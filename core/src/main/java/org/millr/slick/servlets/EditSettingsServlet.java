package org.millr.slick.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.CharEncoding;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.CurrentUserService;
import org.millr.slick.services.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
	    resourceTypes = "sling/servlet/default",
	    selectors = "edit",
	    extensions = "json",
	    methods = "POST"
	)
public class EditSettingsServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EditSettingsServlet.class);
	
	@Reference
    SettingsService settingsService;
	
	@Reference
    CurrentUserService userService;
	
	private static final String BLOG_NAME_PROPERTY = "blogName";
	
	private static final String ANALYTICS_SCRIPT_PROPERTY = "analyticsScript";
	
	private static final String USE_DISPATCHER_PROPERTY = "useDispatcher";
	
	private static final String TEMPORARY_DIRECTORY_PROPERTY = "temporaryDirectory";
	
	private static final String DEFAULT_HEADER_IMAGE = "defaultImage";

	@Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
		
		final PrintWriter writer = response.getWriter();
		
		final boolean allowWrite = userService.getAuthorable(request.getResourceResolver());
		
		response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType("application/json");

        if (allowWrite) {
            final String blogName = request.getParameter(BLOG_NAME_PROPERTY);
            final String analyticsScript = request.getParameter(ANALYTICS_SCRIPT_PROPERTY);
            final boolean useDispatcher = Boolean.parseBoolean(request.getParameter(USE_DISPATCHER_PROPERTY));
            final String defaultHeaderImage = request.getParameter(DEFAULT_HEADER_IMAGE);

            final Map<String, Object> properties = new HashMap<String, Object>();

            properties.put(SettingsService.SYSTEM_BLOG_NAME, blogName);
            properties.put(SettingsService.SYSTEM_ANALYTICS_SCRIPT, analyticsScript);
            properties.put(SettingsService.SYSTEM_USE_DISPATCHER, useDispatcher);
            properties.put(SettingsService.SYSTEM_HEADER_IMAGE, defaultHeaderImage);
            boolean result = settingsService.setProperties(properties);

            if (result) {
                response.setStatus(SlingHttpServletResponse.SC_OK);
                sendResponse(writer, "OK", "Settings successfully updated.");
            } else {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                sendResponse(writer, "Error", "Settings failed to update.");
            }
        } else {
            response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
            sendResponse(writer, "Error", "Current user not authorized.");
        }
	}
	
	protected void sendResponse(final PrintWriter writer, final String header, final String message) {
        try {
            JSONObject json = new JSONObject();

            json.put("header", header);
            json.put("message", message);

            writer.write(json.toString());
            
        } catch (JSONException e) {
            LOGGER.error("Could not write JSON", e);

                writer.write(String.format("{\"header\" : \"%s\", \"message\" : \"%s\"}", header, message));
        }
    }
}