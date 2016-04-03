/*
 * Copyright 2016 Chris Millar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.millr.slick.components.author;

import java.util.Objects;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class Login {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Login.class);

    /**
     * The request.
     */
    private SlingHttpServletRequest request;
    
    private ResourceResolver resolver;
    
    public String reason;
    
    public String loginRedirect;
    
    public Boolean isAdminDefault;
    
    /**
     * Constructor.
     */
    public Login(SlingHttpServletRequest request) {
        this.request = request;
        this.resolver = request.getResourceResolver();
    }
    
    public Boolean getIsAdminDefault() {
        Boolean result = false;
        
        // Get our current resolver.
        ResourceResolver resolver = request.getResourceResolver();
        
        // Adapt to a session and get the current User ID.
        JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);
        String currentUser = session.getUserID();
        
        // Don't check this if we're not logged in as admin.
        if (Objects.equals(currentUser, new String("admin"))) {
            try {
                
                Repository repository = session.getRepository();
                
                // Create a new session by logging into using the default admin username and password.
                Session adminSession = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
                adminSession.logout();
                
                result = true;
                
            } catch (RepositoryException e) {
                LOGGER.debug("Repo Exception: ", e);
                result = false;
            }
        }
        
        return result;
    }
    
    /**
     * Determine where we are going. If we cannot, send to main author page.
     */
    public String getLoginRedirect() {
        String loginRedirect = request.getParameter("resource");
        if (StringUtils.isEmpty(loginRedirect) || Objects.equals(loginRedirect, new String("/"))) {
            loginRedirect = SlickConstants.AUTHOR_PATH + SlickConstants.PAGE_EXTENSION;
        }
        return loginRedirect;
    }

    public String getReason() {
        return request.getParameter(AuthenticationHandler.FAILURE_REASON);
    }
}