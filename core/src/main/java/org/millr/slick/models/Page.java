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
package org.millr.slick.models;

import java.util.Calendar;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.CommentService;
import org.millr.slick.utils.TrimString;

/**
 * Page Class - The page model that most resources use.
 */
@Model(adaptables = { Resource.class })
public class Page
{
    private final Resource resource;
    
    @Inject @Optional
    private String title;
    
    @Inject @Optional
    private String content;
    
    private String description;
    
    @Inject @Optional
    private String[] tags;
    
    @Inject @Optional
    private String image;
    
    @Inject @Optional @Named("jcr:createdBy")
    private String userId;
    
    @Inject @Optional @Named("jcr:created")
    private Calendar created;
    
    @Inject @Optional
    private Calendar publishDate;
    
    private Author author;
    
    @Inject @Optional
    private String slickType;
    
    @Inject @Optional
    private String publishStatus;
    
    @Optional
    public String name;
    
    @Optional
    public String path;
    
    public String link;
    
    public ValueMap properties;
    
    public String guid;
    
    public Iterator<Page> children;
    
    @Inject
    private CommentService commentService;
    
    public Page(final Resource resource) {
        this.resource = resource;
        this.author = resource.adaptTo(Author.class);
        this.properties = getProperties();
    }
    
    public String getName() {
        return resource.getName();
    }
    
    public String getPath() {
        return resource.getPath();
    }
    
    public String getLink() {
        // TODO: This desperately needs to be a sling filter.
        String fullPath = resource.getPath();
        // Replace /content/slick for all URLs.
        String noContent = fullPath.replace("/content/slick", "");
        // Replace /publish for all public URLs.
        String noPublish = noContent.replace("/publish", "") + SlickConstants.PAGE_EXTENSION;
        return noPublish;
    }
    
    public String getGuid() throws RepositoryException {
        Node node = resource.adaptTo(Node.class);
        return node.getIdentifier();
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getDescription() {
        if(properties.containsKey("description")) {
            this.description = properties.get("description", (String)"");
        } else {
            TrimString ts = new TrimString(content,140,false);
            this.description = ts.trimmedString;
        }        
        return description;
    }
    
    public String[] getTags() {
        return tags;
    }
    
    public String getImage() {
        return image;
    }

    public ValueMap getProperties()
    {
        return resource.adaptTo(ValueMap.class);
    }
    
    public Calendar getCreated()
    {
        return created;
    }
    
    public String getPublishStatus()
    {
        return publishStatus;
    }
    
    public Calendar getPublishDate()
    {
        return publishDate;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getSlickType()
    {
        return slickType;
    }
    
    public Iterator<Page> getChildren()
    {
        Iterator<Resource> childs = resource.getChildren().iterator();
        return ResourceUtil.adaptTo(childs,Page.class);
    }
    
    public Iterator<Resource> getComments()
    {
        return commentService.getComments(getName());
    }
}