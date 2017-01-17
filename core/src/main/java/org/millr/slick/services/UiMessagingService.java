package org.millr.slick.services;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;

public interface UiMessagingService {
    
    public void sendResponse(final SlingHttpServletResponse response, 
            int responseCode, 
            final String responseType, 
            final String responseMessage, 
            final JSONObject content);
    
}