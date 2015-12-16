package org.millr.slick.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class User
{
	private Resource resource;
	
	public String displayName;
	
	public String email;

	public User(final Resource resource) {
        this.resource = resource;
    }
}