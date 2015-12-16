package org.millr.slick.utils;

import javax.script.Bindings;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.scripting.sightly.pojo.Use;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class WCMUse.
 * Duplicate WCMUse functionality from AEM.
 */
public class WCMUse implements Use {
	
	/** The Constant LOGGER. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(WCMUse.class);
	
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
	 */
	private void activate() {
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
	
}