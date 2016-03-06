package org.millr.slick.components.common;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class ListPosts {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListPosts.class);
    
    private SlingHttpServletRequest request;
    
    private ResourceResolver resourceResolver;
    
    private Session session;
    
    private static final String PAGE_SIZE_PROPERTY = "pageSize";
    
    @OSGiService
	private PostService postService = null;
    
    private NodeIterator posts;

    public ListPosts(SlingHttpServletRequest request)
    {
    	this.request = request;
    	this.resourceResolver = this.request.getResourceResolver();
    	this.session = this.resourceResolver.adaptTo(Session.class);
    }
    
    public NodeIterator getPosts() {
    	final Long offset = getOffset();
    	posts = postService.getPosts(session, offset, SlickConstants.PAGINATION_SIZE);
    	return posts;
    }
    
    private Long getOffset() {
        Long offset = 0L;
        String currentPage = request.getParameter(SlickConstants.PAGINATION_QUERY_STRING);
        if (currentPage != null) {
            try {
                offset = Long.valueOf(currentPage);
            } catch (NumberFormatException e) {
                LOGGER.error("Could not get offset", e);
            }
        }
        if (offset <= 1) {
            return 0L;
        } else {
            return (offset - 1) * SlickConstants.PAGINATION_SIZE;
        }
    }
}