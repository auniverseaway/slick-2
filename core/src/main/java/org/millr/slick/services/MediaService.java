package org.millr.slick.services;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

public interface MediaService {
	
	NodeIterator getMedia(Session session);
	
	NodeIterator getMedia(Session session, Long offset, Long limit);
	
	long getMediaPageCount(Session session, Long pageSize);
	
	public long getTotalMediaCount(Session session);
}