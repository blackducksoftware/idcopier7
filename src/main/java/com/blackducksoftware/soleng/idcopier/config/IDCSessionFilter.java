/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;

/**
 * @author Ari Kamen
 * @date Sep 18, 2014
 * 
 */
public class IDCSessionFilter implements Filter {

	static Logger log = Logger.getLogger(IDCSessionFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		HttpSession session = request.getSession(false);
		String url = request.getServletPath();

		if (!url.equals(IDCPathConstants.LOGIN_MAIN_PATH)) {
			if (session == null) {
				req.getRequestDispatcher(IDCPathConstants.LOGIN_REDIRECT).forward(request, response);
				return;
			}
		}

		chain.doFilter(req, res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}