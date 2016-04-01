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
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "media",
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

    @Reference
    private UploadService uploadService;
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug(">>>> Entering doPost");
        
        String image = uploadService.uploadFile(request, SlickConstants.MEDIA_PATH);
        
        response.setStatus(SlingHttpServletResponse.SC_OK);
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType("application/json");
        
        JSONObject json = new JSONObject();
        try {
            json.put("header", "Upload");
            json.put("message", "Success: " + image);
        } catch (JSONException e) {
            LOGGER.info("Could not add to JSON object." + e.getMessage());
            e.printStackTrace();
        }
        
        final PrintWriter writer = response.getWriter();
        writer.write(json.toString());
        
    }
}