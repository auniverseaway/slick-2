package org.millr.slick.models;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.millr.slick.SlickConstants;
import org.millr.slick.utils.TrimString;

@Model(adaptables = { Resource.class })
public class Comment {
	
	private final Resource resource;
	
	@Inject 
	private String author;
	
	@Inject
    private String comment;
	
	@Inject
    private String status;
	
	@Inject @Optional @Named("jcr:created")
    private Calendar created;
	
	public Comment(final Resource resource) {
        this.resource = resource;
	}
	
	public String getName() {
		return resource.getName();
	}
	
	public String getPath() {
	    return resource.getPath();
	}
	
	public String getExternalPath() {
	String fullPath = getPath();
	return fullPath.replace(SlickConstants.PUBLISH_PATH, "");
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String getParent() {
	    return resource.getParent().getName();
	}
	
	public String getShortComment() {
		TrimString ts = new TrimString(comment, 50, true);
        return ts.trimmedString;
	}
	
	public String getStatus() {
		return status;
	}
	
	public Calendar getCreated() {
		return created;
	}
	
}