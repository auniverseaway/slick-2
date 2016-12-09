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
package org.millr.slick.services;

import java.util.Map;

public interface SettingsService {
    
    String SYSTEM_BLOG_NAME = "system.blogName";
    
    String SYSTEM_BLOG_DESCRIPTION = "system.blogDescription";

    String SYSTEM_USE_DISPATCHER = "system.useDispatcher";
    
    public static final String SYSTEM_ANALYTICS_SCRIPT = "system.analyticsScript";
    
    public static final String SYSTEM_HEADER_IMAGE = "system.headerImage";
    
    public static final String SYSTEM_ACCENT_COLOR = "system.accentColor";

    boolean setProperties(final Map<String, Object> analyticsProperties);

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
    
    /**
     * Get the blog description.
     *
     * @return The description of the blog.
     */
    String getBlogDescription();

    /**
     * Set the the blog description.
     *
     * @param description The description of the blog.
     * @return true if the save was successful.
     */
    boolean setBlogDescription(final String description);

    /**
     * Get the setting for using a dispatcher.
     *
     * @return The setting for the dispatcher.
     */
    boolean getUseDispatcher();

    /**
     * Set the value for using a dispatcher.
     *
     * @param value The setting for the dispatcher.
     * @return true if the save was successful.
     */
    boolean setUseDispatcher(final boolean value);
    
    /**
     * Get the analytics script.
     *
     * @return The analytics script.
     */
    String getAnalyticsScript();
    
    /**
     * Set the analytics script.
     *
     * @param script The analytics script.
     * @return true if the save was successful.
     */
    boolean setAnalyticsScript(final String script);
    
    String getDefaultHeaderImage();
    
    boolean setDefaultHeaderImage(final String accentColor);
    
    String getAccentColor();
    
    boolean setAccentColor(final String accentColor);
    
}