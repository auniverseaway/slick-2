package org.millr.slick.services;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Provides the API for service that allows file uploads. Files
 * are mostly images uploaded by the author but could be any
 * file type.
 */
public interface UploadService {

    String uploadFile(SlingHttpServletRequest request, String path);
    
}