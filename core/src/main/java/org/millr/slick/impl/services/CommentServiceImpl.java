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
import org.apache.sling.api.resource.ValueMap;
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

    @Override
    public Resource createComment(Resource item, Map<String,Object> commentProperties) {
        LOGGER.info("Creating a comment.");
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

    private void setMixin(Resource resource, String mixinName) {
        try {
            Node node = resource.adaptTo(Node.class);
            node.addMixin(mixinName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}