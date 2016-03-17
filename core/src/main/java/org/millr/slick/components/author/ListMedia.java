package org.millr.slick.components.author;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class ListMedia {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListMedia.class);
    
    private SlingHttpServletRequest request;
    
    private ResourceResolver resourceResolver;
    
    private Session session;
    
    @OSGiService
	private MediaService mediaService = null;
    
    private NodeIterator media;

    public ListMedia(SlingHttpServletRequest request)
    {
    	LOGGER.info(">>>> In ListMedia Constructor");
    	this.request = request;
    	this.resourceResolver = this.request.getResourceResolver();
    	this.session = this.resourceResolver.adaptTo(Session.class);
    }
    
    public NodeIterator getMedia() {
    	final Long offset = getOffset();
    	media = mediaService.getMedia(session, offset, 9L);
    	return media;
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
            return (offset - 1) * 9L;
        }
    }
}