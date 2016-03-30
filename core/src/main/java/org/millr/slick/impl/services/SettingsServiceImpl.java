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

import java.util.Map;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.millr.slick.services.SettingsService;
import org.millr.slick.services.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Constants;

/**
 * The Settings Service Implementation.
 * 
 * This service allows for setting specific configurations 
 * for the application specific to a site.
 */
@Service(value = SettingsService.class)
@Component(metatype = true,
           immediate = true,
           name = "org.millr.slick",
           label = "Slick Configuration",
           description = "General blog settings.")
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION,
              value = "General Configurations"),
    @Property(name = Constants.SERVICE_VENDOR,
              value = "Slick"),
    @Property(name = SettingsServiceImpl.SYSTEM_BLOG_NAME,
              value = SettingsServiceImpl.BLOG_NAME_DEFAULT_VALUE,
              label = "Blog Name",
              description = "The blog name is used throughout your site."),
    @Property(name = SettingsServiceImpl.SYSTEM_BLOG_DESCRIPTION,
              value = SettingsServiceImpl.BLOG_DESCRIPTION_DEFAULT_VALUE,
              label = "Blog Description",
              description = "The blog description is used throughout your site."),
    @Property(name = SettingsServiceImpl.SYSTEM_ACCENT_COLOR,
		      value = SettingsServiceImpl.ACCENT_COLOR_DEFAULT_VALUE,
		      label = "Accent Color",
		      description = "The accent color used throughout your site."),
    @Property(name = SettingsServiceImpl.SYSTEM_ANALYTICS_SCRIPT,
    		  value = SettingsServiceImpl.ANALYTICS_SCRIPT_DEFAULT_VALUE,
    		  label = "Analytics Script",
    		  description = "The script provided by your analytics service. This should include the <script> tag."),
    @Property(name = SettingsServiceImpl.SYSTEM_HEADER_IMAGE,
			  value = SettingsServiceImpl.HEADER_IMAGE_DEFAULT_VALUE,
			  label = "Header Image",
			  description = "The default post header image if one has not been set."),
    @Property(name = SettingsServiceImpl.SYSTEM_USE_DISPATCHER,
              boolValue = SettingsServiceImpl.USE_DISPATCHER_DEFAULT_VALUE,
              label = "Use Dispatcher",
              description = "Using a dispatcher will trigger an Apache cache flush on content modification.")
})
public class SettingsServiceImpl implements SettingsService {

    /** Service to get and set OSGi properties. */
    @Reference
    private OsgiService osgiService;

    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);

    /** PID of the current OSGi component */
    private static final String COMPONENT_PID = "org.millr.slick";

    /** Default value for the blog name */
    public static final String BLOG_NAME_DEFAULT_VALUE = "Slick Blogging Engine";
    
    /** Default value for the blog description */
    public static final String BLOG_DESCRIPTION_DEFAULT_VALUE = "Blogging for the modern web.";
    
    /** Default value for the analytics script */
    public static final String ANALYTICS_SCRIPT_DEFAULT_VALUE = "";
    
    /** Default value for the analytics script */
    public static final String ACCENT_COLOR_DEFAULT_VALUE = "009444";
    
    /** Default value for the analytics script */
    public static final String HEADER_IMAGE_DEFAULT_VALUE = "/etc/slick/designs/slick/img/default-header-background.jpg";

    /** Default value for using a dispatcher. */
    public static final boolean USE_DISPATCHER_DEFAULT_VALUE = false;

    /** Activate Service */
    @Activate
    protected void activate(Map<String, Object> properties) {
    }

    public String getBlogName() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_BLOG_NAME, BLOG_NAME_DEFAULT_VALUE);
    }
    
    public boolean setBlogName(final String name) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_BLOG_NAME, name);
    }
    
    public String getBlogDescription() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_BLOG_DESCRIPTION, BLOG_DESCRIPTION_DEFAULT_VALUE);
    }
    
    public boolean setBlogDescription(final String description) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_BLOG_DESCRIPTION, description);
    }
    
    public String getAnalyticsScript() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_ANALYTICS_SCRIPT, ANALYTICS_SCRIPT_DEFAULT_VALUE);
    }
    
    public boolean setAnalyticsScript(final String script) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_ANALYTICS_SCRIPT, script);
    }
    
    public String getDefaultHeaderImage() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_HEADER_IMAGE, HEADER_IMAGE_DEFAULT_VALUE);
    }
    
    public boolean setDefaultHeaderImage(final String imageUri) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_HEADER_IMAGE, imageUri);
    }
    
    public String getAccentColor() {
    	return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_ACCENT_COLOR, ACCENT_COLOR_DEFAULT_VALUE);
    }
    
    public boolean setAccentColor(final String accentColor) {
    	return osgiService.setProperty(COMPONENT_PID, SYSTEM_ACCENT_COLOR, accentColor);
    }

    public boolean getUseDispatcher() {
        return osgiService.getBooleanProperty(COMPONENT_PID, SYSTEM_USE_DISPATCHER, USE_DISPATCHER_DEFAULT_VALUE);
    }

    public boolean setUseDispatcher(final boolean value) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_USE_DISPATCHER, value);
    }
    
    public boolean setProperties(final Map<String, Object> properties) {
        return osgiService.setProperties(COMPONENT_PID, properties);
    }

}