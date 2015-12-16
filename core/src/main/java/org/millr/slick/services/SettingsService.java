package org.millr.slick.services;

import java.util.Map;

public interface SettingsService {

    /** OSGi property name for the blog name */
    String BLOG_NAME = "slick.blogName";

    /**
     * Set multiple properties for the System Settings service.
     *
     * This is useful for setting multiple properties as the same
     * time in that the OSGi component will only be updated once
     * and thus reset only once.
     *
     * @param properties A map of properties to set.
     * @return true if save was successful.
     */
    boolean setProperties(final Map<String, Object> properties);

    /**
     * Get the name of the blog.
     *
     * @return The name of the blog.
     */
    String getBlogName();

    /**
     * Set the name of the blog.
     *
     * @param name The name of the blog.
     * @return true if the save was successful.
     */
    boolean setBlogName(final String name);
}