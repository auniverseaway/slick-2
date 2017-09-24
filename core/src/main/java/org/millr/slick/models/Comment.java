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
	
	public String getItemPath() {
	    String fullPath = getPath();
	    fullPath = fullPath.replace(SlickConstants.COMMENTS_PATH, "");
	    fullPath = fullPath.replace("/" + resource.getName(), ".html");
	    return fullPath;
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