package org.millr.slick.services;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

public interface PostService {
	
	NodeIterator getPosts(Session session);
	
	NodeIterator getPosts(Session session, Long offset, Long limit);
	
	long getTotalPages(Session session, Long pageSize);
	
}