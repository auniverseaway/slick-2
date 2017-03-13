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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component
public class CommentServiceImpl implements CommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    private static final String COMMENT_QUERY = 
    		  "SELECT * FROM [%s] AS s "
            + "WHERE CONTAINS(s.status, '%s') "
            + "AND ISDESCENDANTNODE(s,'/content/slick/publish/%s') "
            + "ORDER BY [%s] DESC";
    
    private static final String ITEM_PUBLIC_COMMENT_QUERY = 
            "SELECT * FROM [%s] AS s "
          + "WHERE CONTAINS(s.status, '%s') "
          + "AND ISDESCENDANTNODE(s,'%s') "
          + "ORDER BY [%s] DESC";
    
    @Reference 
    private ResourceResolverFactory resourceFactory;
    
    Resource commentsResource;
    
    private static Long commentsCount;
    
    /**
     * Get comments for a specific item.
     */
    @Override
    public Iterator<Resource> getComments(Resource item) {
        LOGGER.info("Getting comments for: " + item.getName());
        Iterator<Resource> itemComments = null;
        try {
            ResourceResolver resolver = getResourceResolver();
            Resource itemResource = getItemResource(resolver, item);
            if (itemResource != null) {
                itemComments = itemResource.getChildren().iterator();
            }
        } catch(Exception e){
            LOGGER.info("There was an error getting comments: " + e.getMessage());
        }
        return itemComments;
    }

    /**
     * Create a comment.
     */
    @Override
    public Resource createComment(Resource item, Map<String,Object> commentProperties) {
        Resource commentResource = null;
        try{
            ResourceResolver resolver = getResourceResolver();
            Resource itemResource = getItemResource(resolver, item);
            String commentName = java.util.UUID.randomUUID().toString();
            commentResource = resolver.create(itemResource, commentName, commentProperties);
            setMixin(commentResource, NodeType.MIX_CREATED);
            resolver.commit();
            resolver.close();
        } catch(Exception e){
            LOGGER.info("There was an error creating the comment: " + e.getMessage());
        }
        return commentResource;
    }

    public ResourceResolver getResourceResolver() {
        ResourceResolver resolver = null;
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "commentService");
        try{
            resolver = resourceFactory.getServiceResourceResolver(paramMap);
            commentsResource = resolver.getResource(SlickConstants.COMMENTS_PATH);
        } catch(Exception e){
            LOGGER.info("There was an error getting the Resource Resolver: " + e.getMessage());
        }
        return resolver;
    }

    private Resource getItemResource(ResourceResolver resolver, Resource item) {
        
        String itemName = item.getName();
        Resource itemResource = null;
        
        ValueMap itemVm = item.adaptTo(ValueMap.class);
        String slickType = itemVm.get("slickType", String.class);
        
        if(slickType == null) {
            slickType = "none";
        }
        
        LOGGER.info("****Getting the item " + itemName + " with " + slickType + "Slick type.");
        
        // If we have a slickType, use it to store our item.
        Resource typeResource = commentsResource.getChild(slickType);
        if(typeResource == null) {
            try {
                Map<String,Object> properties = new HashMap<String,Object>();
                properties.put("jcr:primaryType", "sling:Folder");
                typeResource = resolver.create(commentsResource, slickType, properties);
                setMixin(typeResource, NodeType.MIX_CREATED);
                resolver.commit();
            } catch (PersistenceException e) {
                e.printStackTrace();
                LOGGER.info("There was an error creating the slick type for the item." + e.getMessage());
            }
        }
        // If we have an item, use it to store our comment.
        itemResource = typeResource.getChild(itemName);
        if(itemResource == null) {
            try {
                Map<String,Object> properties = new HashMap<String,Object>();
                properties.put("jcr:primaryType", "sling:OrderedFolder");
                itemResource = resolver.create(typeResource, itemName, properties);
                setMixin(itemResource, NodeType.MIX_CREATED);
                resolver.commit();
            } catch (PersistenceException e) {
                e.printStackTrace();
                LOGGER.info("There was an error getting the item for comment." + e.getMessage());
            }
        }
        return itemResource;
    }

    /**
     * Get all comments
     */
	@Override
	public NodeIterator getComments(Session session, Long offset, Long limit, String status) {
		
		String currentQuery = String.format(COMMENT_QUERY,
                                            SlickConstants.NODE_COMMENT_TYPE,
                                            status,
                                            "comments",
                                            "jcr:created");
        
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
        commentsCount = nodes.getSize();
        return nodes;
	}
	
	/**
	 * Get all site comments based on status.
	 * @param session The current session.
	 * @param status The desired status.
	 * @return all of the given posts.
	 */
	private NodeIterator getComments(Session session, String status) {
        NodeIterator allPosts = getComments(session, null, null, status);
        return allPosts;
    }
	
	/**
	 * Count all the comments for a given pagination size. Used for pagination.
	 */
	@Override
	public long getTotalComments(Session session, String status, Long pageSize) {
        long posts = getNumberOfComments(session, status);
        long totalPages = (long)Math.ceil((double)posts / pageSize);
        return totalPages;
    }
	
	/**
	 * Get the public comments for the item requested.
	 */
	@Override
	public NodeIterator getItemPublicComments(Resource item, String status) {
	    NodeIterator nodes = null;
	    try {
            ResourceResolver resolver = getResourceResolver();
            Resource itemResource = getItemResource(resolver, item);
            if (itemResource != null) {
                String itemPath = itemResource.getPath();
                String currentQuery = String.format(ITEM_PUBLIC_COMMENT_QUERY,
                       SlickConstants.NODE_COMMENT_TYPE,
                       status,
                       itemPath,
                       "jcr:created");
                Session session = resolver.adaptTo(Session.class);
                QueryManager queryManager = session.getWorkspace().getQueryManager();
                Query query = queryManager.createQuery(currentQuery, Query.JCR_SQL2);
                QueryResult result = query.execute();
                nodes = result.getNodes();
            }
        } catch(Exception e){
            LOGGER.info("There was an error getting comments: " + e.getMessage());
        }
	    return nodes;
	}

	/**
	 * Get the number of all comments.
	 */
	@Override
	public Long getNumberOfComments(Session session, String status) {
	    return getComments(session, status).getSize();
	}
	
	private void setMixin(Resource resource, String mixinName) {
        try {
            Node node = resource.adaptTo(Node.class);
            node.addMixin(mixinName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}