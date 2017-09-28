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
public class CommentPagination {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentPagination.class);
    
    private SlingHttpServletRequest request;
    
    private ResourceResolver resourceResolver;
    
    private Session session;
    
    @Inject @Default(values="approved")
    public String status;
    
    @OSGiService
    private CommentService commentService = null;
    
    public long currentPage = 1;
    
    private long totalPageCount = 0L;
    
    private long totalPages = 0;
    
    private String resourcePath;
    
    public CommentPagination(SlingHttpServletRequest request){
        this.request = request;
        this.resourceResolver = this.request.getResourceResolver();
        this.session = this.resourceResolver.adaptTo(Session.class);
        this.resourcePath = getResourcePath();
    }
    
    /**
     * Gets the resource path.
     *
     * Get the short resource path as seen by the front-end. 
     * If we're at the root of a site, assume we are looking 
     * at paginating posts (the default home page). This  
     * definitely needs to be handled in a more elegant way.
     *
     * @return the resource path
     */
    public String getResourcePath() {
        RequestPathInfo rpi = request.getRequestPathInfo();
        String fullPath = rpi.getResourcePath();
        // Replace /content/slick for all URLs.
        String noContent = fullPath.replace("/content/slick", "");
        // Replace /publish for all public URLs.
        String shortResourcePath = noContent.replace("/publish", "");
        return shortResourcePath;
    }
    
    public String getOlderPosts() {
        long totalPages = getTotalPageCount();
        long currentPage = getCurrentPage();
        
        LOGGER.info("TOTAL PAGES: " + totalPages);
        LOGGER.info("CURRENT PAGE: " + currentPage);
        
        String uri = null;
        if (currentPage < totalPages){
            uri = resourcePath + "." + (currentPage + 1) + ".html#" + status;
        }
        return uri;
    }
    
    public String getNewerPosts() {
        long currentPage = getCurrentPage();
        String uri = null;
        if (currentPage > 2){
            uri = resourcePath + "." + (currentPage - 1) + ".html#" + status;
        }
        if (currentPage == 2){
            uri = resourcePath + ".html#" + status;
        }
        return uri;
    }
    
    public long getCurrentPage() {
        
        // By default, we're on the first page.
        long offset = 1;
        
        // Get the page param.
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        String currentPage = pathInfo.getSelectorString();
        
        // If we're not null, convert the value to an Int.
        if (currentPage != null) {
            try {
                offset = Long.valueOf(currentPage);
            } catch (NumberFormatException e) {
                LOGGER.error("Could not get offset", e);
            }
        }
        return offset;
    }
    
    public long getTotalPageCount() {
        long pages = 0;
        pages = commentService.getTotalComments(session, status, SlickConstants.PAGINATION_SIZE);
        return pages;
    }
    
}