package org.millr.slick.components.common;

import java.util.Iterator;

import javax.inject.Inject;
import javax.jcr.query.Query;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class Search {
	private static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
	
	private final SlingHttpServletRequest request;
	
	private ResourceResolver resourceResolver;
	
	private static final String initialQuery = "SELECT * FROM [slick:page] AS s "
									         + "WHERE contains(s.*, '%s') "
									         + "AND ISCHILDNODE(s,'" + SlickConstants.POSTS_PATH + "') "
									         + "ORDER BY [publishDate] ASC";
	
	public String query;
	
	public Iterator<Page> results;
	
	public Search(final SlingHttpServletRequest request) {
		this.request = request;
		this.query = request.getParameter("query");
        this.resourceResolver = this.request.getResourceResolver();
    }
	
	public String getQuery() {
		return query;
	}
	
	public Iterator<Page> getResults() {
		String searchQuery = String.format(initialQuery, query);
		Iterator<Resource> childs = resourceResolver.findResources(searchQuery, Query.JCR_SQL2);
		return ResourceUtil.adaptTo(childs,Page.class);
    }
	
}