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
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.services.CurrentUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class CurrentUser {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUser.class);
    
    private SlingHttpServletRequest request;
    
    private ResourceResolver resourceResolver;
    
    private String displayName;
    
    @OSGiService
    private CurrentUserService currentUserService = null;

    public CurrentUser(SlingHttpServletRequest request) {
        this.request = request;
        this.resourceResolver = this.request.getResourceResolver();
    }
    
    public String getDisplayName() {
        String displayName;
        try {
            displayName = currentUserService.getFirstName(resourceResolver);
        } catch (Exception e) {
            displayName = currentUserService.getId(resourceResolver);
        }
        return displayName;
    }
}