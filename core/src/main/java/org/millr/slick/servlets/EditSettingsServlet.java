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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.CurrentUserService;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.SettingsService;
import org.millr.slick.services.settings.AnalyticsService;
import org.millr.slick.utils.Externalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "edit",
        extensions = "json",
        methods = "POST"
    )
public class EditSettingsServlet extends SlingAllMethodsServlet {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EditSettingsServlet.class);

    private static final long serialVersionUID = 1L;
    
    @Reference
    SettingsService settingsService;
    
    @Reference
    AnalyticsService analyticsService;
    
    @Reference
    DispatcherService dispatcherService;
    
    @Reference
    CurrentUserService userService;
    
    private static final String BLOG_NAME_PROPERTY = "blogName";
    
    private static final String BLOG_DESCRIPTION_PROPERTY = "blogDescription";
    
    private static final String ACCENT_COLOR_PROPERTY = "accentColor";
    
    private static final String ANALYTICS_SCRIPT_PROPERTY = "analyticsScript";
    
    private static final String ANALYTICS_SERVICE_NAME_PROPERTY = "analyticsServiceName";
    
    private static final String ANALYTICS_HEAD_SCRIPT_PROPERTY = "analyticsHeadScript";
    
    private static final String ANALYTICS_FOOT_SCRIPT_PROPERTY = "analyticsFootScript";
    
    private static final String USE_DISPATCHER_PROPERTY = "useDispatcher";
    
    private static final String DEFAULT_HEADER_IMAGE = "defaultImage";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        final boolean allowWrite = userService.getAuthorable(request.getResourceResolver());
        
        int statusCode;
        
        if (allowWrite) {
            
            // Main Settings
            final String blogName = request.getParameter(BLOG_NAME_PROPERTY);
            final String blogDescription = request.getParameter(BLOG_DESCRIPTION_PROPERTY);
            final String accentColor = request.getParameter(ACCENT_COLOR_PROPERTY);
            final String analyticsScript = request.getParameter(ANALYTICS_SCRIPT_PROPERTY);
            final boolean useDispatcher = Boolean.parseBoolean(request.getParameter(USE_DISPATCHER_PROPERTY));
            final String defaultHeaderImage = request.getParameter(DEFAULT_HEADER_IMAGE);
            
            final Map<String, Object> properties = new HashMap<String, Object>();

            properties.put(SettingsService.SYSTEM_BLOG_NAME, blogName);
            properties.put(SettingsService.SYSTEM_BLOG_DESCRIPTION, blogDescription);
            properties.put(SettingsService.SYSTEM_ACCENT_COLOR, accentColor);
            properties.put(SettingsService.SYSTEM_ANALYTICS_SCRIPT, analyticsScript);
            properties.put(SettingsService.SYSTEM_USE_DISPATCHER, useDispatcher);
            properties.put(SettingsService.SYSTEM_HEADER_IMAGE, defaultHeaderImage);
            boolean result = settingsService.setProperties(properties);
            
            // Analytics Settings
            final String serviceName = request.getParameter(ANALYTICS_SERVICE_NAME_PROPERTY);
            final String headScript = request.getParameter(ANALYTICS_HEAD_SCRIPT_PROPERTY);
            final String footScript = request.getParameter(ANALYTICS_FOOT_SCRIPT_PROPERTY);
            
            final Map<String, Object> analyticsProperties = new HashMap<String, Object>();
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_SERVICE_NAME, serviceName);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_HEAD_SCRIPT, headScript);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_FOOT_SCRIPT, footScript);
            
            boolean analyticsResult = analyticsService.setProperties(analyticsProperties);
            
            flushDispatch(request);
            
            if (result && analyticsResult) {
                statusCode = SlingHttpServletResponse.SC_OK;
                sendResponse(response, statusCode, "success", "Settings successfully updated.");
            } else {
                statusCode = SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                sendResponse(response, statusCode, "error", "Settings failed to update.");
            }
        } else {
            statusCode = SlingHttpServletResponse.SC_FORBIDDEN;
            sendResponse(response, statusCode, "error", "Current user not authorized.");
        }
    }
    
    private void flushDispatch(SlingHttpServletRequest request) {
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        dispatcherService.flush(currentDomain, "flushContent");
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
}