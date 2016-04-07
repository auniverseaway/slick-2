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
package org.millr.slick.utils;

import javax.script.Bindings;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.sightly.pojo.Use;

/**
 * WCMUse Utility
 * Duplicate AEM WCMUse Class.
 */
public class WCMUse implements Use {
    
    /** The bindings. */
    private Bindings bindings;
    
    /* (non-Javadoc)
     * @see org.apache.sling.scripting.sightly.pojo.Use#init(javax.script.Bindings)
     */
    @Override
    public void init(Bindings bindings) {
        this.bindings = bindings;
        activate();
    }

    /**
     * Activate.
     * Called when activating the class. This should be overridden.
     */
    public void activate() {
    }
    
    /**
     * Gets the request.
     *
     * @return the request
     */
    public SlingHttpServletRequest getRequest() {
        return (SlingHttpServletRequest)bindings.get(SlingBindings.REQUEST);
    }
    
    /**
     * Gets the resource.
     *
     * @return the resource
     */
    public Resource getResource() {
        return (Resource)bindings.get(SlingBindings.RESOURCE);
    }
    
    /**
     * Gets the resource resolver.
     *
     * @return the resource resolver
     */
    public ResourceResolver getResourceResolver() {
        return getResource().getResourceResolver();
    }
    
    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public ValueMap getProperties() {
        return getResource().adaptTo(ValueMap.class);
    }
    
    /**
     * Gets the script helper.
     *
     * @return the script helper.
     */
    public SlingScriptHelper getSlingScriptHelper() {
        return (SlingScriptHelper)bindings.get(SlingBindings.SLING);
    }
    
}