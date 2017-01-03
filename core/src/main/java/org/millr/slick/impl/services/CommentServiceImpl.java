package org.millr.slick.impl.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component
public class CommentServiceImpl implements CommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Reference 
    private ResourceResolverFactory resourceFactory;
    
    Resource commentsResource;
    
    @Override
    public Iterator<Resource> getComments(String itemName) {
        LOGGER.info("Getting comments for: " + itemName);
        Iterator<Resource> itemComments = null;
        try {
            ResourceResolver resolver = getResourceResolver();
            Resource itemForComment = getItemForComment(resolver, itemName);
            if (itemForComment != null) {
                itemComments = itemForComment.getChildren().iterator();
            }
        } catch(Exception e){
            LOGGER.info("There was an error getting comments: " + e.getMessage());
        }
        return itemComments;
    }

    @Override
    public Resource createComment(String itemName, Map<String,Object> commentProperties) {
        LOGGER.info("Creating a comment.");
        Resource commentResource = null;
        try{
            ResourceResolver resolver = getResourceResolver();
            Resource itemForComment = getItemForComment(resolver, itemName);
            String commentName = java.util.UUID.randomUUID().toString();
            commentResource = resolver.create(itemForComment, commentName, commentProperties);
            setMixin(commentResource, NodeType.MIX_CREATED);
            resolver.commit();
            resolver.close();
        } catch(Exception e){
            LOGGER.info("There was an error creating the comment: " + e.getMessage());
        }
        return commentResource;
    }

    public ResourceResolver getResourceResolver() {
        LOGGER.info("Getting the Resource Resolver.");
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

    private Resource getItemForComment(ResourceResolver resolver, String itemName) {
        LOGGER.info("Getting the item to comment on: " + itemName);
        Resource itemForComment = commentsResource.getChild(itemName);
        if(itemForComment == null) {
            try {
                
                Map<String,Object> properties = new HashMap<String,Object>();
                properties.put("jcr:primaryType", "sling:OrderedFolder");
                itemForComment = resolver.create(commentsResource, itemName, properties);
                setMixin(itemForComment, NodeType.MIX_CREATED);
                resolver.commit();
            } catch (PersistenceException e) {
                e.printStackTrace();
                LOGGER.info("There was an error getting the item for comment." + e.getMessage());
            }
        }
        return itemForComment;
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