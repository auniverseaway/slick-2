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
import javax.json.JsonException;
import javax.json.JsonObject;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.utils.Externalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FlushDispatcherServlet.
 */
@SlingServlet(paths="/bin/slick/flushDispatcher", methods="POST", metatype=false)
public class FlushDispatcherServlet extends SlingAllMethodsServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2493423999579581972L;
    
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FlushDispatcherServlet.class);
    
    @Reference
    private DispatcherService dispatcherService;
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        
        // Get our externally facing URL
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        
        String flushType = request.getParameter("flushType");
        
        // Make our flush response
        JsonObject flushResponse = dispatcherService.flush(currentDomain, flushType);
        
        // Grab our HTTP status
        int status;
        try {
            status = flushResponse.getInt("responseCode");
        } catch (JsonException e) {
            LOGGER.debug("Could not get response code" + e.getMessage());
            status = 500;
        }
        
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(flushResponse.toString());
    }
    
}