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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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
    public JsonObject flush(String domain, String flushType) {
        JsonObject response = Json.createObjectBuilder().build();

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
    public JsonObject flushAll(String domain) {
        JsonObjectBuilder responseBuilder = Json.createObjectBuilder();

        JsonObject uiResponse = doFlush(domain, "/etc");
        JsonObject contentResponse = doFlush(domain, "/content");
        LOGGER.info(uiResponse.toString());
        try {
            responseBuilder.add("responseCode", 200);
            responseBuilder.add("responseType", "success");
            responseBuilder.add("responseMessage", "All cache successfully flushed.");
            responseBuilder.add("uiResponse", uiResponse);
            responseBuilder.add("contentResponse", contentResponse);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObject response = responseBuilder.build();
        return response;
    }

    @Override
    public JsonObject flushUi(String domain) {
        return doFlush(domain, "/etc");
    }

    @Override
    public JsonObject flushContent(String domain) {
        return doFlush(domain, "/content");
    }
    
    @Override
    public JsonObject flushContent(String domain, String path) {
    	if(canFlush()) {
    		return doFlush(domain, path);
    	} else {
    		return buildResponse(304, "error", "No dispatcher to flush");
    	}
    }

    /**
     * Do flush.
     *
     * @param domain the domain
     * @param path the path
     * @return the JSON object
     */
    private JsonObject doFlush(String domain, String path) {

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
    private JsonObject buildResponse(int responseCode, String responseType, String responseMessage) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        try {
            jsonBuilder.add("responseCode", responseCode);
            jsonBuilder.add("responseType", responseType);
            jsonBuilder.add("responseMessage", responseMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JsonObject json = jsonBuilder.build();
        return json;
    }

}