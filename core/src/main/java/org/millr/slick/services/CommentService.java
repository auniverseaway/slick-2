package org.millr.slick.services;

import java.util.Iterator;
import java.util.Map;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;

public interface CommentService {

    public Iterator<Resource> getComments(Resource item);
    
    public NodeIterator getComments(Session session, Long offset, Long limit, String status);
    
    public NodeIterator getItemPublicComments(Resource item, String status);
    
    public long getTotalComments(Session session, String status, Long pageSize);
    
    public Long getNumberOfComments(Session session, String status);

    public Resource createComment(Resource item, Map<String,Object> properties);
	
}