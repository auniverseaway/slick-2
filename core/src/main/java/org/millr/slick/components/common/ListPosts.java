/*
 * Copyright 2016 Chris Millar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.millr.slick.components.common;

import javax.inject.Inject;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
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
    
    @Inject @Via("resource")
    public String slickType;
    
    @OSGiService
	private PostService postService = null;
    
    private NodeIterator posts;

    private Long totalPosts;

    public ListPosts(SlingHttpServletRequest request)
    {
    	this.request = request;
    	this.resourceResolver = this.request.getResourceResolver();
    	this.session = this.resourceResolver.adaptTo(Session.class);
    }
    
    public NodeIterator getPosts() {
    	final Long offset = getOffset();
    	posts = postService.getPosts(session, offset, SlickConstants.PAGINATION_SIZE, slickType);
    	return posts;
    }

    public Long getTotalPosts() {
        return postService.getNumberOfPosts(session);
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
    
    public String getSlickType()
    {
    	return slickType;
    }
}