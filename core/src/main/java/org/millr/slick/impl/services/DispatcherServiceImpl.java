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
package org.millr.slick.impl.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.SettingsService;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Dispatcher Service Implementation.
 * 
 * This service determines if a dispatcher is turned on 
 * in settings. It has various methods to flush different 
 * nodes depending on need. It will return a standardized 
 * Slick Response JSON Object.
 */
@Service
@Component(metatype = true,
		   name = "Dispatcher Service")
@Properties({
    @Property(name = "name", value = "Slick Dispatcher Service"),
    @Property(name = Constants.SERVICE_PID, value = "org.millr.slick.impl.services.DispatcherServiceImpl"),
    @Property(name = Constants.SERVICE_DESCRIPTION, value = "A service to flush a dispatch server.")
})
public class DispatcherServiceImpl implements DispatcherService {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServiceImpl.class);
    
    @Reference
    private SettingsService settingsService;
    
    @Override
    public JSONObject flush(String domain, String flushType) {
        JSONObject response = new JSONObject();
        
        if (canFlush()) {
            if (Objects.equals(flushType, new String("flushAll"))) {
                LOGGER.info("FLUSH TYPE " + flushType);
                response = flushAll(domain);
            }
            if (Objects.equals(flushType, new String("flushUi"))) {
                response = flushUi(domain);
            }
            if (Objects.equals(flushType, new String("flushContent"))) {
                response = flushContent(domain);
            }
        } else {
            response = buildResponse(304, "error", "No dispatcher to flush");
        }
        
        return response;
        
    }
    
    @Override
    public Boolean canFlush() {
        return settingsService.getUseDispatcher();
    }

    @Override
    public JSONObject flushAll(String domain) {
        JSONObject response = new JSONObject();
        JSONObject uiResponse = doFlush(domain, "/etc");
        JSONObject contentResponse = doFlush(domain, "/content");
        LOGGER.info(uiResponse.toString());
        try {
            response.put("responseCode", 200);
            response.put("responseType", "success");
            response.put("responseMessage", "All cache successfully flushed.");
            response.put("uiResponse", uiResponse);
            response.put("contentResponse", contentResponse);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public JSONObject flushUi(String domain) {
        return doFlush(domain, "/etc");
    }

    @Override
    public JSONObject flushContent(String domain) {
        return doFlush(domain, "/content");
    }

    /**
     * Do flush.
     *
     * @param domain the domain
     * @param path the path
     * @return the JSON object
     */
    private JSONObject doFlush(String domain, String path) {

        HttpURLConnection urlConn = null;
        InputStream inStream = null;
        String responseType;
        String responseMessage;
        int responseCode;

        try {

            final URL url = new URL(domain + "/dispatcher/invalidate.cache");
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setAllowUserInteraction(false);
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("accept", "application/json");
            urlConn.setRequestProperty("CQ-Action", "Activate");
            urlConn.setRequestProperty("CQ-Handle", path);

            urlConn.getResponseMessage();
            responseMessage = path + " cache successfully flushed";
            responseCode = 200;
            responseType = "success";

        } catch (final MalformedURLException e) {
            responseMessage = "URL not valid";
            responseCode = 406;
            responseType = "error";
        } catch (final IOException e) {
            responseMessage = "IO Exception";
            responseCode = 500;
            responseType = "error";
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (final IOException e) {
                    LOGGER.debug("IOException while closing stream: " + e.getMessage(), e);
                }
            }
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        
        return buildResponse(responseCode, responseType, responseMessage);

    }
    
    /**
     * Build the response.
     *
     * @param responseMessage the response message
     * @param responseCode the response code
     * @param responseType the response type
     * @return the JSON object
     */
    private JSONObject buildResponse(int responseCode, String responseType, String responseMessage) {
        JSONObject json = new JSONObject();
        try {
            json.put("responseCode", responseCode);
            json.put("responseType", responseType);
            json.put("responseMessage", responseMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}