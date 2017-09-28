package org.millr.slick.servlets.settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.millr.slick.services.CurrentUserService;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.UiMessagingService;
import org.millr.slick.services.settings.CommentService;
import org.millr.slick.utils.Externalizer;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "edit.comments",
        extensions = "json",
        methods = "POST"
    )
public class EditCommentsServlet extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = 7084482438544318666L;
    
    @Reference
    CurrentUserService userService;
    
    @Reference
    DispatcherService dispatcherService;
    
    @Reference
    CommentService commentService;
    
    @Reference
    private UiMessagingService uiMessagingService;
    
    private static final String SYSTEM_COMMENTS_DEFAULT_STATUS_PROPERTY = "commentDefaultStatus";
    
    private static final String SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY_PROPERTY = "commentSiteKey";
    
    private static final String SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY_PROPERTY = "commentSecretKey";
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        int responseCode;
        String responseType;
        String responseMessage;
        JsonObjectBuilder responseContentBuilder = Json.createObjectBuilder();
        
        final boolean allowWrite = userService.getAuthorable(request.getResourceResolver());
        
        if (allowWrite) {
            final String defaultStatus = request.getParameter(SYSTEM_COMMENTS_DEFAULT_STATUS_PROPERTY);
            final String siteKey = request.getParameter(SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY_PROPERTY);
            final String secretKey = request.getParameter(SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY_PROPERTY);
            
            final Map<String, Object> commentProperties = new HashMap<String, Object>();
            commentProperties.put(CommentService.SYSTEM_COMMENTS_DEFAULT_STATUS, defaultStatus);
            commentProperties.put(CommentService.SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY, siteKey);
            commentProperties.put(CommentService.SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY, secretKey);
            
            boolean commentResult = commentService.setProperties(commentProperties);
            
            flushDispatch(request);
            
            if(commentResult) {
                responseCode = 200;
                responseType = "success";
                responseMessage = "success";
            } else {
                responseCode = 500;
                responseType = "error";
                responseMessage = "error";
            }
            
        } else {
            responseCode = 500;
            responseType = "error";
            responseMessage = "error";
        }
        JsonObject responseContent = responseContentBuilder.build();
        uiMessagingService.sendResponse(response, responseCode, responseType, responseMessage, responseContent);
    }
    
    private void flushDispatch(SlingHttpServletRequest request) {
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        dispatcherService.flush(currentDomain, "flushContent");
    }
}