package org.millr.slick.impl.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.millr.slick.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentServiceImpl implements CommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Reference 
    private ResourceResolverFactory resourceFactory;

    @Override
    public void createComment(String itemName) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "CommentService");
        ResourceResolver resourceResolver = null;
        try{
            resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
            Resource testResource = resourceResolver.getResource("/content/slick/publish/posts/test");
            LOGGER.info("TEST RESOURCE NAME: " + testResource.getName());
        } catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }
}