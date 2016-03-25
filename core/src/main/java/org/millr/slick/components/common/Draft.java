package org.millr.slick.components.common;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.models.Page;

@Model(adaptables = SlingHttpServletRequest.class)
public class Draft {
    
    private ResourceResolver resolver;
    
    public Page page;
    
    public Draft(SlingHttpServletRequest request) {
        this.resolver = request.getResourceResolver();
        String draftPath = request.getParameter("resource");
        Resource resource = resolver.getResource(draftPath);
        this.page = resource.adaptTo(Page.class);
    }
}