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
	
	/** OSGi property name for the blog name **/
	String SYSTEM_BLOG_NAME = "system.blogName";

    /** OSGi property name for using a dispatcher **/
	String SYSTEM_USE_DISPATCHER = "system.useDispatcher";
	
	/** OSGi property name for the analytics script **/
	public static final String SYSTEM_ANALYTICS_SCRIPT = "system.analyticsScript";
	
	/** OSGi property name for the analytics script **/
	public static final String SYSTEM_HEADER_IMAGE = "system.headerImage";
	
	/** OSGi property name for the analytics script **/
	public static final String SYSTEM_ACCENT_COLOR = "system.accentColor";

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