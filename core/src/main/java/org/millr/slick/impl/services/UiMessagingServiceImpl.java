package org.millr.slick.impl.services;

import java.io.IOException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.millr.slick.services.UiMessagingService;

@Service
@Component
public class UiMessagingServiceImpl implements UiMessagingService {

    @Override
    public void sendResponse(SlingHttpServletResponse response, int responseCode, String responseType,
            String responseMessage, JsonObject content) {
        
        JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
        try {
            responseBuilder.add("responseCode", responseCode);
            responseBuilder.add("responseType", responseType);
            responseBuilder.add("responseMessage", responseMessage);
            if(content != null) {
                responseBuilder.add("content", content);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObject responseJson = responseBuilder.build();
        
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType("application/json");
        response.setStatus(responseCode);
        
        try {
            response.getWriter().write(responseJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
