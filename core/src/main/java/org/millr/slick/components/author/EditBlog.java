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
package org.millr.slick.components.author;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;

@Model(adaptables = SlingHttpServletRequest.class)
public class EditBlog {

    private SlingHttpServletRequest request;
    
    private Resource resource;
    
    public String slickType;
    
    public String path;
    
    public String editPath;
    
    public Page post;
    
    public EditBlog(SlingHttpServletRequest request) throws Exception {
        this.request = request;
        this.resource = request.getResource();
        
        // Get the Slick Type for the edit page
        ValueMap properties = resource.adaptTo(ValueMap.class);
        slickType = properties.get("slickType", String.class);
        
        // Are we editing an existing post?
        path = request.getParameter("edit");
        if (path != null) {
            // Build the content node
            StringBuilder builder = new StringBuilder();
            editPath = builder.append(SlickConstants.PUBLISH_PATH).append("/").append(slickType).append("/").append(path).toString();
            
            ResourceResolver resourceResolver = resource.getResourceResolver();
            Resource postResource = resourceResolver.getResource(editPath);
            post = postResource.adaptTo(Page.class);
        }
        
        
        
    }
    
}