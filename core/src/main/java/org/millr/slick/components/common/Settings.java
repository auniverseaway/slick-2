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
package org.millr.slick.components.common;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.millr.slick.services.SettingsService;
import org.millr.slick.utils.WCMUse;

/**
 * Sightly component to get System Settings such as blog name.
 */
public class Settings extends WCMUse {

    /** The Sling Script Helper to get the OSGi service. */
    private SlingScriptHelper scriptHelper;

    /** The current resource. */
    private Resource resource;

    private String blogName;
    
    private String analyticsScript;
    
    private String headerImage;
    
    private String accentColor;

    private boolean useDispatcher;

    @Override
    public void activate() {
        scriptHelper = getSlingScriptHelper();
        resource = getResource();

        SettingsService settingsService = scriptHelper.getService(SettingsService.class);

        if (settingsService != null) {
            blogName = settingsService.getBlogName();
            analyticsScript = settingsService.getAnalyticsScript();
            useDispatcher = settingsService.getUseDispatcher();
            headerImage = settingsService.getDefaultHeaderImage();
            accentColor = settingsService.getAccentColor();
        }
    }

    /**
     * Get the name of the blog.
     *
     * @return The name of the blog.
     */
    public String getBlogName() {
        return blogName;
    }
    
    public String getAnalyticsScript() {
    	return analyticsScript;
    }
    
    public String getHeaderImage() {
    	return headerImage;
    }
    
    public String getAccentColor() {
    	return accentColor;
    }

    /**
     * Get the setting for using a dispatcher.
     *
     * @return The setting for using a dispatcher.
     */
    public boolean getUseDispatcher() {
        return useDispatcher;
    }
}