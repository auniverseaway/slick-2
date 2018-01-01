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
package org.millr.slick.servlets.item;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.security.Privilege;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.UploadService;
import org.millr.slick.utils.Externalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
	    resourceTypes = "sling/servlet/default",
	    selectors = "edit",
	    extensions = "html",
	    methods = "POST"
	)
public class EditItemServlet extends SlingAllMethodsServlet {
	
	/**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EditItemServlet.class);
	
    /**
     * The generated serialVersionUID.
     */
    private static final long serialVersionUID = 169080260544691827L;
	
	private ResourceResolver resolver;
	
	private Session session;
	
	@Reference
    private UploadService uploadService;
	
	@Reference
    private DispatcherService dispatcherService;
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
	// response.setContentType("text/html; charset=UTF-8");
	// response.setCharacterEncoding("UTF-8");
	request.setCharacterEncoding("UTF-8");
	LOGGER.debug(">>>> Entering doPost");
		
		resolver = request.getResourceResolver();
		session = resolver.adaptTo(Session.class);
		
		final String title = request.getParameter("title");
		final String name = request.getParameter("nodeName");
		final String content = request.getParameter("content");
		final String description = request.getParameter("description");
		final String[] tags =  request.getParameterValues("tags");
		final String slickType = request.getParameter("slickType");
		final String resourceType = slickType.substring(0, slickType.length()-1);
		final String publishString = request.getParameter("publishDate");
		final String publishStatus = request.getParameter("publishStatus");
		final boolean enableComments = Boolean.parseBoolean(request.getParameter("enableComments"));
		
		// Upload our image
		String image = uploadService.uploadFile(request, SlickConstants.MEDIA_PATH);
		
		// Get our parent resource
		Resource myResource = resolver.getResource(SlickConstants.PUBLISH_PATH + "/" + slickType);
		
		// Create a properties Map to store our params.
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put(JcrConstants.JCR_PRIMARYTYPE, "slick:page");
		properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "slick/publish/" + resourceType + "/detail");
		properties.put("title", title);
		properties.put("content", content);
		properties.put("slickType", slickType);
		properties.put("publishStatus", publishStatus);
		properties.put("enableComments", enableComments);
		
		// Check to see if description is empty
		if(!description.isEmpty()){
			properties.put("description", description);
		}
		
		// Set Published Date
		Calendar publishCalendar = Calendar.getInstance();
		// If the publish request param is set, use it.
		if(!publishString.isEmpty()){
			Date publishDate = convertDate(publishString);
			publishCalendar.setTime(publishDate);
		}
		properties.put("publishDate", publishCalendar);
		
		// Set Tags
		if (tags != null) {
            properties.put("tags", tags);
        }
		
		// Set Image
		if (image != null) {
            properties.put("image", image);
        }
		
		// Try to get the existing resource.
		String existingPath = myResource.getPath() + "/" + name;
		Resource post = resolver.getResource(existingPath);
		
		// Update or create our resource
		if (post != null) {
			LOGGER.info("Saving existing post.");
			ModifiableValueMap existingProperties = post.adaptTo(ModifiableValueMap.class);
			existingProperties.putAll(properties);
			
			// If all tags were removed on an existing post, remove them from existing properties
			if (tags == null){
				existingProperties.remove("tags");
			}
			setMixin(post, NodeType.MIX_LAST_MODIFIED);
		} else {
			LOGGER.info("Creating New Post.");
			post = resolver.create(myResource, name, properties);
			setMixin(post, NodeType.MIX_CREATED);
		}
		
		// Set the status of our post
		try {
            setStatus(post);
        } catch (RepositoryException e) {
            LOGGER.debug("Could not update post status. " + e.getMessage());
        }
		
		// Commit and close our work
		resolver.commit();
		resolver.close();
		
		// Build redirect path
		String redirectPath = post.getPath() + SlickConstants.PAGE_EXTENSION;
		if(Objects.equals(publishStatus, new String("draft"))) {
		    redirectPath = SlickConstants.DRAFT_PATH + ".html?resource=" + post.getPath();
		}
		
		// Flush our dispatcher
		flushDispatch(request);
		
		// Send our redirect
		response.sendRedirect(redirectPath);
		LOGGER.debug("<<<< Leaving doPost");
	}

	/**
	 * Sets the status of a post.
	 * Always clear the status, but set a deny to everyone if post is set as draft.
	 * Authors will still be able to update the post.
	 *
	 * @param post the new status
	 * @throws RepositoryException the repository exception
	 */
	private void setStatus(Resource post) throws RepositoryException {
        
	    ValueMap properties = post.adaptTo(ValueMap.class);
	    String publishStatus = (String) properties.get("publishStatus", (String) null);
	    Node postNode = post.adaptTo(Node.class);
        String postPath = postNode.getPath();
        
        // Clear any ACLs
        AccessControlUtils.clear(session, postPath);
	    if(Objects.equals(publishStatus, new String("draft"))) {
	        AccessControlUtils.denyAllToEveryone(session, postPath);
            AccessControlUtils.allow(postNode, "authors", Privilege.JCR_ALL);
        }
	    session.save();
	}

    private void flushDispatch(SlingHttpServletRequest request) {
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        dispatcherService.flush(currentDomain, "flushContent");
    }

    private void setMixin(Resource post, String mixinName) {
		try {
        	Node postNode = post.adaptTo(Node.class);
        	postNode.addMixin(mixinName);
        } catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private Date convertDate(String publishString) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		LocalDateTime dateTime = LocalDateTime.parse(publishString, formatter);
		
		Date publishDate = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		return publishDate;
	}
}