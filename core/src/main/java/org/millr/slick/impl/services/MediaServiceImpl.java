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

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.MediaService;
import org.millr.slick.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Media Service Implementation.
 * 
 * This service will get a list of media nodes.
 */
@Service
@Component
public class MediaServiceImpl implements MediaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
    
    private static final String MEDIA_QUERY = "SELECT * FROM [nt:file] AS s "
                                            + "WHERE "
                                            + "ISCHILDNODE(s,'/content/slick/publish/media') "
                                            + "ORDER BY [%s] DESC";

    public NodeIterator getMedia(Session session) {
        return getMedia(session, null, null);
    }
    
    public NodeIterator getMedia(Session session, Long offset, Long limit) {
        
        String currentQuery = String.format(MEDIA_QUERY,"jcr:created");
        
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

    public long getMediaPageCount(Session session, Long pageSize) {
        long posts = getTotalMediaCount(session);
        return (long)Math.ceil((double)posts / pageSize);
    }
    
    public long getTotalMediaCount(Session session) {
        return getMedia(session).getSize();
    }
    
}