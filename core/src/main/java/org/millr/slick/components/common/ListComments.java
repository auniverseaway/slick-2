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
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class ListComments {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListComments.class);
    
    private SlingHttpServletRequest request;
    
    private ResourceResolver resourceResolver;
    
    private Session session;
    
    @Inject @Default(values="approved")
    public String status;
    
    @OSGiService
    private CommentService commentService = null;
    
    private NodeIterator comments;

    private Long totalComments;

    public ListComments(SlingHttpServletRequest request)
    {
    	LOGGER.info("listing comments");
        this.request = request;
        this.resourceResolver = this.request.getResourceResolver();
        this.session = this.resourceResolver.adaptTo(Session.class);
    }
    
    public NodeIterator getComments() {
        final Long offset = getOffset();
        comments = commentService.getComments(session, offset, SlickConstants.PAGINATION_SIZE, status);
        return comments;
    }

    public Long getTotalComments() {
        totalComments = commentService.getNumberOfComments(session, status);
        return totalComments;
    }
    
    private Long getOffset() {
        Long offset = 0L;
        
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        String currentPage = pathInfo.getSelectorString();
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