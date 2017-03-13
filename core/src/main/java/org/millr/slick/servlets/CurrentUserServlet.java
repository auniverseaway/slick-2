package org.millr.slick.servlets;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.CurrentUserService;
import org.millr.slick.services.UiMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(paths="/bin/slick/getCurrentUser", methods="GET", metatype=false)
public class CurrentUserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 8769581589662680558L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserServlet.class);
    
    @Reference
    private UiMessagingService uiMessagingService;
    
    @Reference
    private CurrentUserService currentUserService;
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        
        int responseCode;
        String responseType;
        String responseMessage;
        JSONObject responseContent = new JSONObject();
        
        ResourceResolver resourceResolver = request.getResourceResolver();
        
        String currentUserId = currentUserService.getUserId(resourceResolver);
        String displayName = currentUserService.getFullName(resourceResolver);
    
        try {
        	responseCode = 200;
            responseType = "success";
            responseMessage = "success";
            responseContent.put("userId", currentUserId);
            responseContent.put("displayName", displayName);
        } catch (JSONException e) {
        	responseCode = 500;
        	responseType = "error";
        	responseMessage = "error";
            e.printStackTrace();
        }
            
        uiMessagingService.sendResponse(response, responseCode, responseType, responseMessage, responseContent);
        
    }
    
}