package org.millr.slick.services;

import java.util.Map;

public interface SettingsService {

	/** OSGi property name for the blog name */
    public static final String SYSTEM_BLOG_NAME = "system.blogName";

    /** OSGi property name for extensionless URLs */
    public static final String SYSTEM_EXTENSIONLESS_URLS = "system.extentionlessUrls";

    /** OSGi property name for the temporary directory */
    public static final String SYSTEM_TEMPORARY_DIRECTORY = "system.temporaryDirectory";

    boolean setProperties(final Map<String, Object> properties);

    String getBlogName();
    
    boolean setBlogName(final String name);
    
    boolean getExtensionlessUrls();
    
    boolean setExtensionlessUrls(final boolean value);
    
	String getTemporaryDirectory();
	
	boolean setTemporaryDirectory(final String directory);	
	
}