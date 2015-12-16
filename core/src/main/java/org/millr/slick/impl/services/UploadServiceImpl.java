package org.millr.slick.impl.services;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.millr.slick.services.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service to handle file uploading.
 */
@Service
@Component
public class UploadServiceImpl implements UploadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);
	
	@Override
	public String uploadFile(SlingHttpServletRequest request, String path) {
		// TODO Auto-generated method stub
		return null;
	}

}