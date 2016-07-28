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

import java.util.Iterator;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.millr.slick.models.Page;

@Model(adaptables = Resource.class)
public class Navigation {
    
    private final Resource resource;
    
    public String link;
    
    private ResourceResolver resourceResolver;
    
    public Iterator<Page> adminHeader;
    
    public Navigation(final Resource resource) {
        resourceResolver = resource.getResourceResolver();
        this.resource = resourceResolver.getResource(SlickConstants.AUTHOR_PATH);
    }
    
    public Iterator<Page> getAdminHeader() {
        String query = "SELECT * FROM [slick:page] AS s WHERE [title] IS NOT NULL and ISCHILDNODE(s,'" + SlickConstants.AUTHOR_PATH + "') ORDER BY [menuOrder] ASC";
        Iterator<Resource> childs = resourceResolver.findResources(query, Query.JCR_SQL2);
        return ResourceUtil.adaptTo(childs,Page.class);
    }
    
    public String getLink() {
        return this.resource.getPath() + SlickConstants.PAGE_EXTENSION;
    }
}