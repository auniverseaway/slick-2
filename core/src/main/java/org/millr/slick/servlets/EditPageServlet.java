package org.millr.slick.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.millr.slick.SlickConstants;
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
     * to the assets folder using the FileUploadService.
     */
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("*** In doPost ***");
		
		ResourceResolver resolver = request.getResourceResolver();
		
		Session session = resolver.adaptTo(Session.class);
		
		final String title = request.getParameter("title");
		final String name = request.getParameter("nodeName");
		final String content = request.getParameter("content");
		final String slickType = request.getParameter("slickType");
		
		Resource myResource = resolver.getResource(SlickConstants.PUBLISH_PATH + "/" + slickType);
		
		String existingPath = myResource.getPath() + "/" + name;
		
		LOGGER.info(existingPath);
		
		Map<String,Object> properties = new HashMap<String,Object>();
		
		properties.put(JcrConstants.JCR_PRIMARYTYPE, "slick:page");
		properties.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, "slick/publish/page");
		properties.put("title", title);
		properties.put("content", content);
		
		// Update or create our resource
		// Try to get the existing.
		Resource post = resolver.getResource(existingPath);
		
		if (post != null) {
			LOGGER.info("NOT NULL");
			ModifiableValueMap existingProperties = post.adaptTo(ModifiableValueMap.class);
			existingProperties.putAll(properties);
		} else {
			LOGGER.info("NULL");
			post = resolver.create(myResource, name, properties);
		}
		
		// Update time stamp and created by
        try {
        	Node postNode = post.adaptTo(Node.class);
			postNode.addMixin(NodeType.MIX_CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        // Commit and close our work
		resolver.commit();
		resolver.close();
		
		response.sendRedirect(post.getPath() + SlickConstants.PAGE_EXTENSION);
		
	}
}