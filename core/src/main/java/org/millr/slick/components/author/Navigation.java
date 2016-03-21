package org.millr.slick.components.author;

import java.util.Iterator;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class Navigation {
	
	private final Resource resource;
	
	public String link;
	
	private ResourceResolver resourceResolver;
	
	public Iterator<Page> adminHeader;
	
	public Navigation(final Resource resource) {
		resourceResolver = resource.getResourceResolver();
		this.resource = resourceResolver.getResource(SlickConstants.AUTHOR_PATH);
    }
	
	public Iterator<Page> getAdminHeader() {
		String query = "SELECT * FROM [slick:page] AS s WHERE [title] IS NOT NULL and ISCHILDNODE(s,'" + SlickConstants.AUTHOR_PATH + "') ORDER BY [menuOrder] ASC";
		Iterator<Resource> childs = resourceResolver.findResources(query, Query.JCR_SQL2);
		return ResourceUtil.adaptTo(childs,Page.class);
    }
	
	public String getLink() {
		return this.resource.getPath() + SlickConstants.PAGE_EXTENSION;
	}
}