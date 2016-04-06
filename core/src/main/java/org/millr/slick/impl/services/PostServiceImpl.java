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
package org.millr.slick.impl.services;

import java.util.Calendar;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Post Service Implementation.
 * 
 * This service gets nodes based on supplied params.
 * It currently will only grab nodes that are not not before today.
 */
@Service
@Component
public class PostServiceImpl implements PostService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
    
    private static final String BLOG_QUERY = "SELECT * FROM [%s] AS s "
                                           + "WHERE s.[%s] <= CAST('%s' AS DATE) "
                                           + "AND ISCHILDNODE(s,'/content/slick/publish/%s') "
                                           + "ORDER BY [%s] DESC";
    
    private static Long postsCount;

    public NodeIterator getPosts(Session session) {
        NodeIterator allPosts = getPosts(session, null, null, "posts");
        return allPosts;
    }
    
    public NodeIterator getPosts(Session session, Long offset, Long limit, String slickType) {
        
        String today = getToday();
        
        String currentQuery = String.format(BLOG_QUERY,
                                            SlickConstants.NODE_POST_TYPE,
                                            "publishDate",
                                            today,
                                            slickType,
                                            "publishDate");
        
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
        postsCount = nodes.getSize();
        return nodes;
    }

    private String getToday() {
        Calendar cal = Calendar.getInstance();
        String today = null;
        try {
            today = ValueFactoryImpl.getInstance().createValue(cal).getString();
        } catch (ValueFormatException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (RepositoryException e1) {
            e1.printStackTrace();
        }
        return today;
    }

    public long getTotalPages(Session session, Long pageSize) {
        long posts = getNumberOfPosts(session);
        long totalPages = (long)Math.ceil((double)posts / pageSize);
        LOGGER.info("TOTAL PAGES: " + totalPages);
        return totalPages;
    }
    
    public long getNumberOfPosts(Session session) {
        return getPosts(session).getSize();
    }
    
}