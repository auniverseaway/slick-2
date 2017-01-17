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

import java.util.Iterator;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.millr.slick.models.Page;
import org.millr.slick.models.Author;
import org.millr.slick.utils.Externalizer;
import org.millr.slick.utils.WCMUse;

public class ViewPage extends WCMUse {
    
    private Resource resource;
    
    private SlingHttpServletRequest request;
    
    private Page page;
    
    private Author author;
    
    private Externalizer external;
    
    private String pageSelector;
    
    @Override
    public void activate() {
        this.request = getRequest();
        this.resource = getResource();
        this.page = resource.adaptTo(Page.class);
        this.author = resource.adaptTo(Author.class);
        this.external = request.adaptTo(Externalizer.class);
        this.pageSelector = request.getRequestPathInfo().getSelectorString();
    }
    
    public String getPageSelector() {
        return pageSelector;
    }

    public Page getPage() {
        return page;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public Externalizer getExternal() {
        return external;
    }
}