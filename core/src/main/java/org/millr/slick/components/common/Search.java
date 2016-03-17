package org.millr.slick.components.common;

import java.util.Iterator;

import javax.inject.Inject;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

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
	
	private Session session;
	
	private static final String initialQuery = "SELECT * FROM [slick:page] AS s "
									         + "WHERE contains(s.*, '%s') "
									         + "AND ISCHILDNODE(s,'" + SlickConstants.POSTS_PATH + "') "
									         + "ORDER BY [publishDate] ASC";
	
	public String query;
	
	public NodeIterator results;
	
	public Long resultsCount;
	
	public Search(final SlingHttpServletRequest request) {
		this.request = request;
		this.query = request.getParameter("query");
        this.resourceResolver = this.request.getResourceResolver();
        this.session = resourceResolver.adaptTo(Session.class);
    }
	
	public String getQuery() {
		return query;
	}
	
	public NodeIterator getResults() {
		
		String searchQuery = String.format(initialQuery, query);
		NodeIterator nodes = null;
		
		try {
			QueryManager queryManager = session.getWorkspace().getQueryManager();
	        Query query = queryManager.createQuery(searchQuery, Query.JCR_SQL2);
	        QueryResult result = query.execute();
	        nodes = result.getNodes();
	    } catch (RepositoryException e) {
			LOGGER.error("Could not search repository", e);
        }
		this.resultsCount = nodes.getSize();
		return nodes;
    }
	
	public Long getResultsCount() {
		return resultsCount;
	}
	
}