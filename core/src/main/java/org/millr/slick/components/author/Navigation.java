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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;
import org.millr.slick.services.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class Navigation {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Navigation.class);
    
    private final Resource resource;
    
    public String link;
    
    private ResourceResolver resourceResolver;
    
    public Iterator<Page> adminHeader;
    
    @OSGiService
    private SettingsService settingsService;
    
    public Navigation(final Resource resource) {
        resourceResolver = resource.getResourceResolver();
        this.resource = resourceResolver.getResource(SlickConstants.AUTHOR_PATH);
    }
    
    public Iterator<Page> getAdminHeader() {
        String query = "SELECT * FROM [slick:page] AS s WHERE [title] IS NOT NULL and ISCHILDNODE(s,'" + SlickConstants.AUTHOR_PATH + "') ORDER BY [menuOrder] ASC";
        Iterator<Resource> navItems = resourceResolver.findResources(query, Query.JCR_SQL2);
        List<Resource> enabledNavItemsList = new ArrayList<>();
        while (navItems.hasNext()) {
            Resource navItem = navItems.next();
            if(Objects.equals("comments", new String(navItem.getName()))) {
                boolean featureStatus = settingsService.checkFeatureStatus("comments");
                if(featureStatus == true) {
                    enabledNavItemsList.add(navItem);
                }
            } else {
                enabledNavItemsList.add(navItem);
            }
            
        }
        Iterator<Resource> enabledNavItems = enabledNavItemsList.iterator();
        return ResourceUtil.adaptTo(enabledNavItems,Page.class);
    }
    
    public String getLink() {
        return this.resource.getPath() + SlickConstants.PAGE_EXTENSION;
    }
}