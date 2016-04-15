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
package org.millr.slick.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class EditMediaServlet.
 * 
 * <p>A servlet to handle uploading media.</p>
 */
@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "edit",
        extensions = "json",
        methods = "POST"
    )
public class EditMediaServlet extends SlingAllMethodsServlet {
    
    /**
     * The Serial Version UID.
     */
    private static final long serialVersionUID = -1605449650842582293L;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EditMediaServlet.class);

    /** The upload service reference. */
    @Reference
    private UploadService uploadService;
    
    /* (non-Javadoc)
     * @see org.apache.sling.api.servlets.SlingAllMethodsServlet#doPost(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug(">>>> Entering Media doPost");
        
        String mediaUrl = uploadService.uploadFile(request, SlickConstants.MEDIA_PATH);
        
        JSONObject content = new JSONObject();
        try {
            content.put("mediaUrl", mediaUrl);
        } catch(Exception e) {
            e.printStackTrace();
        }     
        
        sendResponse(response, SlingHttpServletResponse.SC_OK, "success", "Uploading media.", content);
        
    }
    
    protected void sendResponse(final SlingHttpServletResponse response, int responseCode, final String responseType, final String responseMessage) {
        sendResponse(response, responseCode, responseMessage, null);
    }
    
    /**
     * Send response.
     *
     * @param response the response
     * @param responseCode the response code
     * @param responseType the response type
     * @param responseMessage the response message
     * @param content the response content
     */
    protected void sendResponse(final SlingHttpServletResponse response, int responseCode, final String responseType, final String responseMessage, final JSONObject content) {
        
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}