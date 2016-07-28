package org.millr.slick.servlets.item;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.utils.Externalizer;

@SlingServlet(
    resourceTypes = "sling/servlet/default",
    selectors = "delete",
    extensions = "json",
    methods = "POST"
)
public class DeleteItemServlet extends SlingAllMethodsServlet {
    
    /**
     * The generated Serial Version UID
     */
    private static final long serialVersionUID = -6459681142830175945L;

    @Reference
    private DispatcherService dispatcherService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        
        ResourceResolver resourceResolver = request.getResourceResolver();
        final String resourceString = request.getParameter("resource");
        Resource resource = resourceResolver.getResource(resourceString);
        resourceResolver.delete(resource);
        resourceResolver.commit();
        
        flushDispatch(request);
        
        sendResponse(response, 200, "success", "Item has been deleted");
    }
    
    protected void sendResponse(final SlingHttpServletResponse response, int responseCode, final String responseType, final String responseMessage) {
        
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON.put("responseCode", responseCode);
            responseJSON.put("responseType", responseType);
            responseJSON.put("responseMessage", responseMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType("application/json");
        response.setStatus(responseCode);
        
        try {
            response.getWriter().write(responseJSON.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void flushDispatch(SlingHttpServletRequest request) {
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        dispatcherService.flush(currentDomain, "flushContent");
    }
}