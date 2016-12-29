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
package org.millr.slick.servlets.settings;

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

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "edit.analytics",
        extensions = "json",
        methods = "POST"
    )
public class EditAnalyticsServlet extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = 5760735584278014301L;

    @Reference
    SettingsService settingsService;
    
    @Reference
    AnalyticsService analyticsService;
    
    @Reference
    DispatcherService dispatcherService;
    
    @Reference
    CurrentUserService userService;
    
    private static final String ANALYTICS_SERVICE_NAME_PROPERTY = "analyticsServiceName";
    
    private static final String ANALYTICS_HEAD_SCRIPT_PROPERTY = "analyticsHeadScript";
    
    private static final String ANALYTICS_FOOT_SCRIPT_PROPERTY = "analyticsFootScript";
    
    private static final String ANALYTICS_REPORT_SUITE_PROPERTY = "analyticsReportSuite";
    
    private static final String ANALYTICS_TWITTER_USERNAME_PROPERTY = "analyticsTwitterUsername";
    
    private static final String ANALYTICS_FACEBOOK_APP_ID_PROPERTY = "analyticsFacebookAppId";
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        final boolean allowWrite = userService.getAuthorable(request.getResourceResolver());
        
        int statusCode;
        
        if (allowWrite) {
            
            // Analytics Settings
            final String serviceName = request.getParameter(ANALYTICS_SERVICE_NAME_PROPERTY);
            final String headScript = request.getParameter(ANALYTICS_HEAD_SCRIPT_PROPERTY);
            final String footScript = request.getParameter(ANALYTICS_FOOT_SCRIPT_PROPERTY);
            final String reportSuite = request.getParameter(ANALYTICS_REPORT_SUITE_PROPERTY);
            final String twitterUsername = request.getParameter(ANALYTICS_TWITTER_USERNAME_PROPERTY);
            final String facebookAppId = request.getParameter(ANALYTICS_FACEBOOK_APP_ID_PROPERTY);
            
            final Map<String, Object> analyticsProperties = new HashMap<String, Object>();
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_SERVICE_NAME, serviceName);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_HEAD_SCRIPT, headScript);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_FOOT_SCRIPT, footScript);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_REPORT_SUITE, reportSuite);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_TWITTER_USERNAME, twitterUsername);
            analyticsProperties.put(AnalyticsService.SYSTEM_ANALYTICS_FACEBOOK_APPID, facebookAppId);
            
            boolean analyticsResult = analyticsService.setProperties(analyticsProperties);
            
            flushDispatch(request);
            
            if (analyticsResult) {
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
            e.printStackTrace();
        }

    }    
}