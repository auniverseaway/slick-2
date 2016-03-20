package org.millr.slick.impl.services;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.JcrConstants;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component
public class PostServiceImpl implements PostService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
	
	private static final String BLOG_QUERY = "SELECT * FROM [%s] AS s "
										   + "WHERE "
										   + "ISCHILDNODE(s,'/content/slick/publish/%s') "
										   + "ORDER BY [%s] DESC";

	public NodeIterator getPosts(Session session) {
		return getPosts(session, null, null, "posts");
    }
	
	public NodeIterator getPosts(Session session, Long offset, Long limit,String slickType) {
		
		String currentQuery = String.format(BLOG_QUERY,
				SlickConstants.NODE_POST_TYPE,
				slickType,
	            "publishDate");
		
		LOGGER.info("CURRENT QUERY: " + currentQuery);
		
		NodeIterator nodes = null;

        if (session != null) {
            try {
                QueryManager queryManager = session.getWorkspace().getQueryManager();
                Query query = queryManager.createQuery(currentQuery, Query.JCR_SQL2);

                if (offset != null) {
                    query.setOffset(offset);
                }

                if (limit != null) {
                    query.setLimit(limit);
                }

                QueryResult result = query.execute();
                nodes = result.getNodes();
            } catch (RepositoryException e) {
                LOGGER.error("Could not search repository", e);
            }
        }
        return nodes;
	}

	public long getTotalPages(Session session, Long pageSize) {
		long posts = getNumberOfPosts(session);
        return (long)Math.ceil((double)posts / pageSize);
    }
	
	public long getNumberOfPosts(Session session) {
        return getPosts(session).getSize();
    }
	
}