package org.millr.slick.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface CurrentUserService {

	void getUser(ResourceResolver resourceResolver);
	
    void getSession(ResourceResolver resourceResolver);
    
    boolean getAuthorable(ResourceResolver resourceResolver);
    
    String getFirstName(ResourceResolver resourceResolver);
    
    String getId(ResourceResolver resourceResolver);
}