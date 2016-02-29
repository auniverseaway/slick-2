package org.millr.slick.components.author;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.services.CurrentUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class CurrentUser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUser.class);
	
	private SlingHttpServletRequest request;
	
	private ResourceResolver resourceResolver;
	
	private String displayName;
	
	@OSGiService
	private CurrentUserService currentUserService = null;

	public CurrentUser(SlingHttpServletRequest request) {
        this.request = request;
        this.resourceResolver = this.request.getResourceResolver();
    }
	
	public String getDisplayName() {
		String displayName;
		try {
			displayName = currentUserService.getFirstName(resourceResolver);
		} catch (Exception e) {
			displayName = currentUserService.getId(resourceResolver);
		}
		return displayName;
	}
}