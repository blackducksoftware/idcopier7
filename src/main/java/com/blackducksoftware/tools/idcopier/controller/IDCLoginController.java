/**
 * IDCopier
 * <p>
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.tools.idcopier.controller;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.tools.idcopier.constants.IDCConfigurationConstants;
import com.blackducksoftware.tools.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.tools.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.tools.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.tools.idcopier.model.IDCConfig;
import com.blackducksoftware.tools.idcopier.model.IDCServer;
import com.blackducksoftware.tools.idcopier.model.IDCSession;
import com.blackducksoftware.tools.idcopier.model.UserServiceModel;
import com.blackducksoftware.tools.idcopier.service.LoginService;
import com.blackducksoftware.tools.idcopier.service.ProjectService;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller to handle display of login page and authentication
 */

@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class IDCLoginController {
    static Logger log = Logger.getLogger(IDCLoginController.class);

    // This injects the configuration file
    @Autowired
    private IDCConfig config;
    @Autowired
    private UserServiceModel userServiceModel;

    // Internal
    private static List<IDCServer> servers = null;
    // Config options
    private static String configJson = null;

    @RequestMapping(value = IDCPathConstants.LOGIN_MAIN_PATH)
    public ModelAndView displayLoginPage(@CookieValue(value = IDCViewModelConstants.IDC_COOKIE_SERVER, required = false) String serverCookie,
                                         @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_USER, required = false) String userNameCookie, @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_PASSWORD, required = false) String passwordCookie) {

        IDCSession session = new IDCSession();

        /**
         * This seems like a painful way to handle cookies...:-/ TODO: Find an alternate way -- AK
         */

        if (userNameCookie != null) {
            session.setUserName(userNameCookie);
        }
        if (passwordCookie != null) {
            session.setPassword(passwordCookie);
        }

        return new ModelAndView(IDCViewConstants.LOGIN_FORM, IDCViewModelConstants.IDC_SESSION, session);
    }

    @RequestMapping(value = IDCPathConstants.LOGIN_REDIRECT)
    public ModelAndView redirect() {
        return new ModelAndView(IDCViewConstants.LOGIN_REDIRECT);
    }

    /**
     * Login method, grabs the user's inputs, calls the loginserver and redirects to the project display page
     * <p>
     * This is the first and only login process that uses the user credentials
     *
     * @param session
     * @param rememberCookie
     * @param response
     * @param httpSession
     * @return
     */
    @RequestMapping(value = IDCPathConstants.LOGIN_PROCESS_PATH)
    public ModelAndView processLogin(@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
                                     @RequestParam(value = IDCViewModelConstants.REMEMBER_COOKIES, required = false, defaultValue = "false") Boolean rememberCookie, HttpServletResponse response, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        LoginService loginService = userServiceModel.getNewLoginService();

        log.info("Logging in...: " + session.getUserName());
        httpSession.removeAttribute(IDCViewModelConstants.IDC_SESSION);
        ProtexServerProxy proxy;
        try {
            // Before we log in, process the servers
            servers = processServerListConfiguration(session);

            if (servers == null || servers.size() == 0) {
                throw new Exception("No servers configured.  Please see README");
            }

            /**
             * This used to grab the first server on the list and try to authenticate against it.
             *
             * It has since been modified to loop through each of the servers until it is able to log in to one of them.
             */
            for (IDCServer server : servers) {
                System.out.println(server.getServerURI());

                try {
                    proxy = loginService.getProxyByServerURI(server.getServerURI());
                    proxy.validateCredentials();
                } catch (Exception e) {
                    log.error("Login failed: " + e.getMessage());

                    server = null;
                }

                if (server != null) {
                    // Process cookie information
                    processCookies(rememberCookie, session, response);

                    modelAndView.addObject(IDCViewModelConstants.IDC_SESSION, session);
                    modelAndView.addObject(IDCViewModelConstants.SERVER_LIST, servers);

                    // Checkboxes
                    Map<String, Boolean> options = new HashMap<String, Boolean>();
                    options.put(IDCViewModelConstants.COPY_DEFER_BOM_REFRESH_OPTION, config.isBomRefreshDefer());
                    options.put(IDCViewModelConstants.COPY_RECURSIVE_OPTION, config.isRecursive());
                    options.put(IDCViewModelConstants.COPY_OVERWRITE_OPTION, config.isOverwriteIDs());
                    options.put(IDCViewModelConstants.PARTIAL_BOM_OPTION, config.istPartialBom());
                    options.put(IDCViewModelConstants.PULL_PARENT_IDS_OPTION, config.isPullParentIds());
                    options.put(IDCViewModelConstants.APPEND_COMMENTS_OPTION, config.isAppendComments());

                    Gson gson = new Gson();
                    configJson = gson.toJson(options);

                    break;
                }
            }

            if (configJson == null) {
                // Create a pretty error message to print to the screen
                StringBuilder message = new StringBuilder();
                message.append("Failed to authenticate against all Protex servers:");

                for(IDCServer server:servers){
                    message.append("<br />");
                    message.append(server.getServerURI());
                }

                throw new Exception(message.toString());
            }
        } catch (Exception e) {
            /**
             * Capture login errors and redirect back to the form
             */
            String msg = e.getMessage();
            if (e.getCause() != null) {
                msg = e.getCause().getMessage();
            }
            modelAndView.addObject(IDCViewModelConstants.LOGIN_ERROR_MSG, msg);
            log.error("Error logging in", e);
            modelAndView.setViewName(IDCViewConstants.LOGIN_FORM);
            return modelAndView;
        }

        modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
        return modelAndView;
    }

    /**
     * Processes a server change from the dropdown Return JSON project list TODO: This actually retreives a project list, consider refactoring?
     *
     * @param session
     * @return
     */
    @RequestMapping(value = IDCPathConstants.LOGIN_NEW_SERVER + IDCPathConstants.LOCATION)
    public String processLoginForServer(@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session, @RequestParam(value = IDCViewModelConstants.IDC_SERVER_NAME) String serverName,
                                        @PathVariable String location, HttpServletResponse response) {

        log.info("Processing a new login for server: " + serverName);
        LoginService loginService = userServiceModel.getLoginService();
        ProjectService projectService = userServiceModel.getProjectService();
        List<ProjectInfo> projects = null;
        try {
            IDCServer server = loginService.getServerByName(serverName);
            ProtexServerProxy proxy = loginService.getProxy(server.getServerName());
            projects = projectService.getProjectsByServer(proxy, server);

        } catch (Exception e) {
            String msg = e.getMessage();
            log.error("Unable to authenticate new server: " + serverName + " cause: " + e.getMessage());
            if (e.getCause() != null)
                msg = e.getCause().getMessage();

            return msg;
        }

        String projectJson = new Gson().toJson(projects);
        log.debug("Sending project JSON:" + projectJson);
        return projectJson;
    }

    /**
     * Parses the property key server.list which contains a list of comma separated addresses. Uses the logged in user's credentials to fill out a list of server beans.
     *
     * @param session
     * @return
     * @throws Exception
     */
    private List<IDCServer> processServerListConfiguration(IDCSession session) throws Exception {
        servers = new ArrayList<IDCServer>();
        log.info("Processing server configuration");
        String serverListLocation = config.getServerListLocation();

        if (serverListLocation != null) {

            File f = new File(serverListLocation);
            if (f.exists()) {

                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String server = br.readLine();
                    while (server != null) {
                        try {
                            IDCServer idcServer = new IDCServer(server, session.getUserName(), session.getPassword());
                            servers.add(idcServer);
                        } catch (IllegalArgumentException iae) {
                            log.error("Unable to determine host name for: " + server);
                        }
                        server = br.readLine();
                    }
                } catch (IOException e) {
                    log.warn("Parsing error", e);
                    throw new Exception(e.getMessage());
                }
                log.info("Parsed server list, count: " + servers.size());

            } else {
                log.warn("Server configuration file does not exist at: " + f);
            }
        } else {
            throw new Exception("No server list available, looking for key: " + IDCConfigurationConstants.SERVER_LIST);
        }

        // Key step that creates internal server maps
        LoginService loginService = userServiceModel.getLoginService();
        loginService.setServers(servers);

        return servers;
    }

    @RequestMapping(IDCPathConstants.SERVERS)
    public String processServers() {
        return new Gson().toJson(servers);
    }

    @RequestMapping(IDCPathConstants.LOGIN_CONFIG)
    public String getConfigOptions() {
        return configJson;
    }

    @RequestMapping(IDCPathConstants.SESSION_INFO)
    public String getVersionInfo(@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session) {
        Map<String, String> sessionDetails = new HashMap<String, String>();
        sessionDetails.put(IDCViewModelConstants.SESSION_USERNAME, session.getUserName());
        return new Gson().toJson(sessionDetails);
    }

    /**
     * @param rememberCookie
     * @param session
     * @param response
     */
    private void processCookies(Boolean rememberCookie, IDCSession session, HttpServletResponse response) {
        // User logged in, save it
        if (rememberCookie) {
            Cookie userCookie = new Cookie(IDCViewModelConstants.IDC_COOKIE_USER, session.getUserName());
            response.addCookie(userCookie);
            Cookie passwordCookie = new Cookie(IDCViewModelConstants.IDC_COOKIE_PASSWORD, session.getPassword());
            response.addCookie(passwordCookie);
        }
    }

    @RequestMapping(IDCPathConstants.LOGOUT_MAIN_PATH)
    public String logout(@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session, @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_USER, required = false) String userNameCookie,
                         @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_PASSWORD, required = false) String passwordCookie) {
        // Message to be sent out to the browser console
        String msg = session.getUserName() + " has been logged out!";

        // Going a bit overkill here, but just trying to be safe since just 'session = null' was not working as expected
        userNameCookie = null;
        passwordCookie = null;
        session.setUserName(userNameCookie);
        session.setPassword(passwordCookie);
        session = null;

        Map<String, String> logoutDetails = new HashMap<String, String>();
        logoutDetails.put(IDCViewModelConstants.LOGOUT_MESSAGE, msg);
        return new Gson().toJson(logoutDetails);
    }
}
