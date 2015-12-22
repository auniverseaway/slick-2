package org.millr.slick.components.author;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;

@Model(adaptables = SlingHttpServletRequest.class)
public class EditBlog {

	private SlingHttpServletRequest request;
	
	private Resource resource;
	
	public String slickType;
	
	public String path;
	
	public String editPath;
	
	public Page post;
	
	public EditBlog(SlingHttpServletRequest request) throws Exception {
		this.request = request;
		this.resource = request.getResource();
		
		// Get the Slick Type for the edit page
		ValueMap properties = resource.adaptTo(ValueMap.class);
		slickType = properties.get("slickType", String.class);
		
		// Are we editing an existing post?
		path = request.getParameter("edit");
		if (path != null) {
			// Build the content node
			StringBuilder builder = new StringBuilder();
			editPath = builder.append(SlickConstants.PUBLISH_PATH).append("/").append(slickType).append("/").append(path).toString();
			
			ResourceResolver resourceResolver = resource.getResourceResolver();
			Resource postResource = resourceResolver.getResource(editPath);
			post = postResource.adaptTo(Page.class);
		}
		
		
		
	}
	
}