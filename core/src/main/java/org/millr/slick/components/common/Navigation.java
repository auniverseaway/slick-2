package org.millr.slick.components.common;

import java.util.Iterator;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;

@Model(adaptables = Resource.class)
public class Navigation {
    
    private Resource resource;
    
    private ResourceResolver resourceResolver;
    
    private Iterator<Page> pages;
    
    public Navigation(final Resource resource) {
        resourceResolver = resource.getResourceResolver();
        this.resource = resourceResolver.getResource(SlickConstants.AUTHOR_PATH);
    }
    
    public Iterator<Page> getPages() {
        String query = "SELECT * "
                     + "FROM [slick:page] "
                     + "AS s WHERE "
                     + "[publishStatus] = 'publish' AND "
                     + "ISCHILDNODE(s,'" + SlickConstants.PAGES_PATH + "') "
                     + "ORDER BY [publishDate] ASC";
        Iterator<Resource> childs = resourceResolver.findResources(query, Query.JCR_SQL2);
        return ResourceUtil.adaptTo(childs,Page.class);
    }
}