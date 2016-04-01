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

import java.io.InputStream;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.millr.slick.services.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service to handle file uploading.
 * Additional Reading: http://labs.6dglobal.com/blog/2013-01-02/handling-file-upload-adobe-cq/
 * Additional Reading: http://www.programcreek.com/java-api-examples/index.php?api=org.apache.jackrabbit.commons.JcrUtils
 */
@Service
@Component
public class UploadServiceImpl implements UploadService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);
    
    @Override
    public String uploadFile(SlingHttpServletRequest request, String path) {
        final RequestParameterMap params = request.getRequestParameterMap();
        ResourceResolver resolver = request.getResourceResolver();
        
        String filePath = null;

        // Loop through the parameters of the request to see if we have a file (not a form field)
        for (final Map.Entry<String, RequestParameter[]> pairs : params.entrySet()) {
            final RequestParameter[] pArr = pairs.getValue();
            final RequestParameter param = pArr[0];

            if (!param.isFormField()) {
                
                final String name = param.getFileName();
                final String mimeType = param.getContentType();
                
                if (!name.isEmpty()) {
                    try {
                        final InputStream stream = param.getInputStream();
                        
                        LOGGER.info("******Image Parent: " + path);
                        LOGGER.info("******Image Name: " + name);
                        LOGGER.info("******Image MimeType: " + mimeType);

                        Resource imagesParent = resolver.getResource(path);
                        Node imageNode = JcrUtils.putFile(imagesParent.adaptTo(Node.class), name, mimeType, stream);
                        resolver.commit();

                        filePath = imageNode.getPath();
                    } catch (javax.jcr.RepositoryException e) {
                        LOGGER.error("Could not save image to repository.", e);
                    } catch (java.io.IOException e) {
                        LOGGER.error("Could not get image input stream", e);
                    }
                }
            }
        }

        return filePath;
    }

}