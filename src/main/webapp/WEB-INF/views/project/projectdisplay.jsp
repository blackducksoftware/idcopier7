<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!--
The initial display after the login.
Contains the server selectors, project pulldown and JSON representing tree paths.
-->

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Protex ID Copier | Main</title>

    <link rel="shortcut icon" href="images/favicon.png" type="image/png"/>
    <link rel="stylesheet" href="css/main.css" type="text/css">
    <link rel="stylesheet" href="css/bootstrap.css" type="text/css">
    <link rel="stylesheet" href="css/Fancytree/skin-bootstrap/ui.fancytree.css" type="text/css">
    <link rel="stylesheet" href="css/justified-nav.css" type="text/css">
    <link rel="stylesheet" href="css/menu.css" type="text/css">
    <link rel="stylesheet" href="css/Animate/animate.min.css" type="text/css">
    <link rel="stylesheet" href="css/HubSpot-messenger/messenger.css" type="text/css">
    <link rel="stylesheet" href="css/HubSpot-messenger/messenger-theme-air.css" type="text/css">
    <!-- <link rel="stylesheet" href="css/HubSpot-messenger/messenger-spinner.css"> -->
</head>
<body>
<div class="main">
    <div>
        <div class="session-details">
            <table>
                <tbody>
                <tr>
                    <td class="username-data"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="header-logo">
            <img src="images/tempIcon.png">
        </div>
    </div>
    <nav class="animate">
        <h1>Menu</h1>
        <div class="divider"></div>

        <ul>
            <li><a href="displayComments" target="_blank"><h4>
                Copy Comments <span class="glyphicon glyphicon-comment"></span>
            </h4></a></li>
            <li><a href="log" target="_blank"><h4>
                View Log <span class="glyphicon glyphicon-list-alt"></span>
            </h4></a></li>
        </ul>

        <div class="divider"></div>
        <ul>
            <li><a href="login" id="logoutButton"><h4>
                Logout <span class="glyphicon glyphicon-user"></span>
            </h4></a></li>
        </ul>
    </nav>
    <div class="nav-controller">
        <table>
            <tbody>
            <tr>
                <td><span
                        class="[ glyphicon glyphicon-align-justify ] controller-open"></span>
                </td>
            </tr>
            </tbody>
        </table>

    </div>
</div>
<hr>
<div class="project-container">
    <div class="col-sm-5 col-lg-5 col-mx-5">
        <div class="well">
            <div class="projects-header-buffer">
                <p class="header-text">Source Project</p>
            </div>

            <div class="progress-bar-section">
                <div class="refresh-progress-button ">
                    <div class="btn-group pull-right btn-spacer">
                        <button type="button"
                                class="btn btn-primary dropdown-toggle btn-xs"
                                data-toggle="dropdown">
                            Refresh <span class="caret"></span>
                        </button>
                        <ul class="rp-dropdown-menu dropdown-menu" role="menu">
                            <li><a href="#"
                                   onclick="activateRefreshFromPullDown(source, partialRefresh)">Partial</a></li>
                            <li><a href="#"
                                   onclick="activateRefreshFromPullDown(source, fullRefresh)">Full</a></li>
                        </ul>
                    </div>
                </div>

                <div class="progress">
                    <div id="sourceProgressBar" class="progress-bar"
                         role="progressbar" data-transitiongoal="100"></div>
                </div>
            </div>

            <table class="table">
                <tbody>
                <tr>
                    <td><select class="form-control selectSourceServer"
                                title="Select Source Server">
                    </select></td>
                </tr>
                <tr>
                    <td><select id="selectSourceProject"
                                name="selectSourceProject"
                                class="form-control selectSourceProject">
                        <option value="1">No Projects</option>
                    </select></td>
                </tr>
                <tr>
                    <td><input class="form-control userSourcePathInput"
                               id="userSourcePathInput" name="userSourcePathInput"
                               placeholder="Source Path"></input></td>
                </tr>
                <tr>
                    <td><i>Selected:</i>
                        <h5 class="sourceSelectedPath"></h5></td>
                </tr>
                <tr>
                    <td></td>
                </tr>
                </tbody>
            </table>
            <div id="sourceCodeTree" data-source="ajax" class="sourceCodeTree"></div>
        </div>
    </div>
    <div class="col-sm-2 col-lg-2">
        <table class="table">
            <tbody>
            <!--  These options are invoked when use clicks perform copy button
                  Behavior is captured in main.js onclick

                  The default behavior is read from the IDCConfig class and dynamically set via main.js
                  The name of the checkbox must match the internal java value
                 -->


            <tr>
                <td><h4>Defer BOM refresh</h4></td>
                <td><input id="deferBomRefreshCheckBox"
                           name="defer-bom-option" class="checkbox" type="checkbox"
                           value="deferBomRefreshCheckBox"></td>
            </tr>
            <tr>
                <td><h4>Recursive</h4></td>
                <td><input id="recursiveCopyCheckBox" name="recursive-option"
                           class="checkbox" type="checkbox" value="recursiveCopyCheckBox"/></td>
            </tr>
            <tr>
                <td><h4>Replace Target Identifications</h4></td>
                <td><input id="overwriteIDsCheckBox" name="overwrite-option"
                           class="checkbox" type="checkbox"/></td>
            </tr>

            <tr>
                <td><h4>Get Parent IDs</h4></td>
                <td><input id="pullParentIDsCheckBox" name="pull-parent-ids-option"
                           class="checkbox" type="checkbox"/></td>
            </tr>

            <!--  Commenting out as part of refresh pulldown change
                    <tr>
                        <td><h4>Partial BOM Refresh</h4></td>
                        <td><input id="partialBOMCheckBox" name="partial-bom-option"
                            class="checkbox" type="checkbox" /></td>
                    </tr>
                -->
            <!--  BUTTONS -->

            <tr>
                <td colspan="2"><input type="button" id="submitCopyButton"
                                       value="Perform Copy" class="btn btn-success perform-copy-button"/></td>
            </tr>
            </tbody>
        </table>
        <div class="version-info">
            <table>
                <tbody>
                <tr>
                    <td class="bold">Version:</td>
                    <td class="version-data">${display_version}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-5 col-lg-5 col-mx-5">
        <div class="well">
            <div class="projects-header-buffer">
                <p class="header-text">Target Project</p>
            </div>
            <div class="progress-bar-section">
                <div class="refresh-progress-button ">
                    <div class="btn-group pull-right btn-spacer">
                        <button type="button"
                                class="btn btn-primary dropdown-toggle btn-xs"
                                data-toggle="dropdown">
                            Refresh <span class="caret"></span>
                        </button>
                        <ul class="rp-dropdown-menu dropdown-menu" role="menu">
                            <li><a href="#"
                                   onclick="activateRefreshFromPullDown('target', partialRefresh)">Partial</a></li>
                            <li><a href="#"
                                   onclick="activateRefreshFromPullDown('target', fullRefresh)">Full</a></li>
                        </ul>
                    </div>
                </div>
                <div class="progress">
                    <div id="targetProgressBar" class="progress-bar"
                         role="progressbar" data-transitiongoal="100"></div>
                </div>
            </div>
            <table class="table">
                <tbody>
                <tr>
                    <td><select class="form-control selectTargetServer"
                                id="selectTargetServer" name="selectTargetServer">
                    </select></td>
                </tr>
                <tr>
                    <td><select id="selectTargetProject"
                                name="selectTargetProject"
                                class="form-control selectTargetProject">
                        <option value="1">No Projects</option>
                    </select></td>
                </tr>
                <tr>
                    <td><input class="form-control userTargetPathInput"
                               id="userTargetPathInput" name="userTargetPathInput"
                               placeholder="Target Path"></input></td>
                </tr>
                <tr>
                    <td><i>Selected:</i>
                        <h5 class="targetSelectedPath"></h5></td>
                </tr>
                <tr>
                    <td></td>
                </tr>
                </tbody>
            </table>
            <div id="targetCodeTree" class="targetCodeTree"></div>
        </div>
    </div>
</div>
</div>
</body>

<!--  JS Libraries -->
<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/libs/jquery-ui/jquery-ui.custom.js"></script>
<script src="js/libs/progress/p-loader.js"></script>
<script src="js/libs/jquery-cookie/jquery.cookie.js"></script>
<script src="js/libs/Fancytree/jquery.fancytree.js"></script>
<script src="js/libs/Fancytree/jquery.fancytree.glyph.js"></script>
<script src="js/libs/Fancytree/jquery.fancytree.persist.js"></script>
<script src="js/libs/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>
<!-- <script src="js/libs/bootstrap-growl/bootstrap-growl.min.js"></script> -->
<script src="js/libs/HubSpot-messenger/messenger.js"></script>
<script src="js/libs/HubSpot-messenger/messenger-theme-future.js"></script>
<script src="js/libs/bootstrap-progressbar/bootstrap-progressbar.js"></script>
<!--  ID Copier JS files -->
<script src="js/tree.js"></script>
<script src="js/menu.js"></script>
<script src="js/progress.js"></script>
<!--  Common JS functions shared between main and comments -->
<script src="js/common.js"></script>
<!--  Keep this last, as main.js invokes other files -->
<script src="js/main.js"></script>

</
html>