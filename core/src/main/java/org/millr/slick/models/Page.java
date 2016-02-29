package org.millr.slick.models;

import java.util.Calendar;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.millr.slick.SlickConstants;
import org.millr.slick.utils.TrimString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class })
public class Page
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Page.class);
	
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
	
	private Author author;
	
	@Inject @Optional
    private String slickType;
	
	@Optional
	public String name;
	
	@Optional
	public String path;
	
	public String link;
	
	public ValueMap properties;
	
	public String guid;
	
	public Iterator<Page> children;
	
	public Page(final Resource resource) {
		LOGGER.info("In Constructor");
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
		return resource.getPath() + SlickConstants.PAGE_EXTENSION;
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
    	LOGGER.info("In get properties.");
        return resource.adaptTo(ValueMap.class);
    }
    
    public Calendar getCreated()
    {
    	return created;
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
}