package org.millr.slick.components.author;

import java.util.Iterator;

import javax.inject.Inject;
import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.millr.slick.models.Page;

@Model(adaptables = Resource.class)
public class ListPosts {

	private final Resource resource;
	
	@Inject
	@Optional
    private String slickType;
	
	public String formattedValue;
	
	public Iterator<Page> children;
	
	public ListPosts(final Resource resource) {
        this.resource = resource;
    }
	
	public Iterator<Page> getChildren() {
		ResourceResolver resourceResolver = resource.getResourceResolver();
		String query = "SELECT * FROM [nt:base] AS s "
				+ "WHERE "
				+ "ISCHILDNODE(s,'/content/slick/publish/" + slickType + "') "
				+ "ORDER BY [created] DESC";
		Iterator<Resource> childs = resourceResolver.findResources(query, Query.JCR_SQL2);
		return ResourceUtil.adaptTo(childs,Page.class);
	}
}