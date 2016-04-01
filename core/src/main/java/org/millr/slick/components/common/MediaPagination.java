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


import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.MediaService;
import org.millr.slick.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class MediaPagination {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Pagination.class);
    
    private SlingHttpServletRequest request;
    
    private ResourceResolver resourceResolver;
    
    private Session session;
    
    @OSGiService
    private MediaService mediaService = null;
    
    public long currentPage = 1;
    
    private long totalPageCount = 0L;
    
    private long totalPages = 0;
    
    private static final String PAGE_QUERY_STRING = "?" + SlickConstants.PAGINATION_QUERY_STRING + "=";
    
    public MediaPagination(SlingHttpServletRequest request){
        this.request = request;
        this.resourceResolver = this.request.getResourceResolver();
        this.session = this.resourceResolver.adaptTo(Session.class);
    }
    
    public String getOlderPosts() {
        long totalPages = getTotalPageCount();
        long currentPage = getCurrentPage();
        String uri = null;
        if (currentPage < totalPages){
            uri = request.getRequestURI() + PAGE_QUERY_STRING + (currentPage + 1);
        }
        return uri;
    }
    
    public String getNewerPosts() {
        long currentPage = getCurrentPage();
        String uri = null;
        if (currentPage > 2){
            uri = request.getRequestURI() + PAGE_QUERY_STRING + (currentPage - 1);
        }
        if (currentPage == 2){
            uri = request.getRequestURI();
        }
        return uri;
    }
    
    public long getCurrentPage() {
        
        // By default, we're on the first page.
        long offset = 1;
        
        // Get the page param.
        String currentPageKey = request.getParameter(SlickConstants.PAGINATION_QUERY_STRING);
        
        // If we're not null, convert the value to an Int.
        if (currentPageKey != null) {
            try {
                offset = Long.valueOf(currentPageKey);
            } catch (NumberFormatException e) {
                LOGGER.error("Could not get offset", e);
            }
        }
        return offset;
    }
    
    public long getTotalPageCount() {
        long pages = 0;
        pages = mediaService.getMediaPageCount(session, 9L);
        return pages;
    }
    
}