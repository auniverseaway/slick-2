package org.millr.slick.servlets.item;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.millr.slick.SlickConstants;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.UiMessagingService;
import org.millr.slick.utils.Externalizer;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "delete",
        extensions = "json",
        methods = "POST"
    )
public class DeleteItemServlet extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = -5413154193188924055L;
    
    @Reference
    DispatcherService dispatcherService;
    
    @Reference
    private UiMessagingService uiMessagingService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        Resource resource;
        int responseCode;
        String responseType;
        String responseMessage;

        JsonObjectBuilder responseContentBuilder = Json.createObjectBuilder();

        //JSONObject responseContent = new JSONObject();
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            final String resourceString = request.getParameter("resource");
            resource = resourceResolver.getResource(SlickConstants.PUBLISH_PATH + resourceString);
            resourceResolver.delete(resource);
            resourceResolver.commit();
            
            flushDispatch(request);
            
            responseCode = 200;
            responseType = "success";
            responseMessage = "success";
            
            responseContentBuilder.add("name", resource.getName());
            responseContentBuilder.add("path", resource.getPath());
        } catch(Exception e) {
            
            responseCode = 500;
            responseType = "error";
            responseMessage = "error - Could not delete item.";
            
            e.printStackTrace();
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