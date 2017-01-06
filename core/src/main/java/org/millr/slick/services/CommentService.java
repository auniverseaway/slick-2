package org.millr.slick.services;

import java.util.Iterator;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

public interface CommentService {

    public Iterator<Resource> getComments(Resource item);

    public Resource createComment(Resource item, Map<String,Object> properties);
	
}