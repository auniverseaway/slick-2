package org.millr.slick.services;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Provide an API to upload files.
 */
public interface UploadService {

    String uploadFile(SlingHttpServletRequest request, String path);
    
}