package org.millr.slick.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.CurrentUserService;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.UploadService;
import org.millr.slick.utils.Externalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
	    resourceTypes = "sling/servlet/default",
	    selectors = "item",
	    extensions = "html",
	    methods = "POST"
	)
public class EditPageServlet extends SlingAllMethodsServlet {
	
	/**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EditPageServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Create or edit the item.
     *
     * Creates blog path and saves properties. If blog resource already
     * exists, the resource is updated with new properties. Saves file
     * to the media folder using the UploadService.
     */
	
	@Reference
    private UploadService uploadService;
	
	@Reference
    private DispatcherService dispatcherService;
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug(">>>> Entering doPost");
		
		ResourceResolver resolver = request.getResourceResolver();
		
		Session session = resolver.adaptTo(Session.class);
		
		final String title = request.getParameter("title");
		final String name = request.getParameter("nodeName");
		final String content = request.getParameter("content");
		final String description = request.getParameter("description");
		final String[] tags = request.getParameterValues("tags");
		final String slickType = request.getParameter("slickType");
		final String resourceType = slickType.substring(0, slickType.length()-1);
		final String publishString = request.getParameter("publishDate");
		
		String image = uploadService.uploadFile(request, SlickConstants.MEDIA_PATH);
		
		Resource myResource = resolver.getResource(SlickConstants.PUBLISH_PATH + "/" + slickType);
		
		String existingPath = myResource.getPath() + "/" + name;
		
		Map<String,Object> properties = new HashMap<String,Object>();
		
		properties.put(JcrConstants.JCR_PRIMARYTYPE, "slick:page");
		properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "slick/publish/" + resourceType + "/detail");
		properties.put("title", title);
		properties.put("content", content);
		properties.put("slickType", slickType);
		
		// Check to see if description is empty
		if(!description.isEmpty()){
			LOGGER.info("Description is not null.");
			properties.put("description", description);
		}
		
		Calendar publishCalendar = Calendar.getInstance();
		
		if(!publishString.isEmpty()){
			Date publishDate = convertDate(publishString);
			publishCalendar.setTime(publishDate);
		}
		
		properties.put("publishDate", publishCalendar);
		
		if (tags != null) {
            properties.put("tags", tags);
        }
		
		if (image != null) {
            properties.put("image", image);
        }
		
		// Update or create our resource
		//// Try to get the existing resource.
		Resource post = resolver.getResource(existingPath);
		
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
		
		// Commit and close our work
		resolver.commit();
		resolver.close();
		
		flushDispatch(request);
		
		response.sendRedirect(post.getPath() + SlickConstants.PAGE_EXTENSION);
		LOGGER.debug("<<<< Leaving doPost");
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