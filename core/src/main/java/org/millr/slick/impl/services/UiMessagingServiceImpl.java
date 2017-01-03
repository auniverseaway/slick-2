package org.millr.slick.impl.services;

import java.io.IOException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.UiMessagingService;

@Service
@Component
public class UiMessagingServiceImpl implements UiMessagingService {

    @Override
    public void sendResponse(SlingHttpServletResponse response, int responseCode, String responseType,
            String responseMessage, JSONObject content) {
        
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON.put("responseCode", responseCode);
            responseJSON.put("responseType", responseType);
            responseJSON.put("responseMessage", responseMessage);
            
            if(content != null) {
                responseJSON.put("content", content);
            }            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType("application/json");
        response.setStatus(responseCode);
        
        try {
            response.getWriter().write(responseJSON.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
