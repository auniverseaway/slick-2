package org.millr.slick.services;

import java.util.Iterator;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

public interface CommentService {

    public Iterator<Resource> getComments(String itemName);

    public Resource createComment(String itemName, Map<String,Object> properties);
	
}