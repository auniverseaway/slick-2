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

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.millr.slick.services.OsgiService;

/**
 * OsgiService Implementation
 * This is the main OSGi Service wrapper for setting 
 * and getting different types of properties from an OSGi Service.
 */
@Service(value = OsgiService.class)
@Component(metatype = true)
@Properties({
    @Property(name = "name", value = "Slick OSGi Service"),
    @Property(name = "description", value = "Programatically set properties of OSGi configurations.")
})
public class OsgiServiceImpl implements OsgiService {

    /** The config admin. */
    @Reference
    private ConfigurationAdmin configAdmin;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(OsgiServiceImpl.class);

    /* (non-Javadoc)
     * @see org.millr.slick.services.OsgiService#setProperty(java.lang.String, java.lang.String, java.lang.Object)
     */
    public boolean setProperty(final String pid, final String property, final Object value) {
        try {
            Configuration conf = configAdmin.getConfiguration(pid);

            @SuppressWarnings("unchecked")
            Dictionary<String, Object> props = conf.getProperties();

            if (props == null) {
                props = new Hashtable<String, Object>();
            }

            props.put(property, value != null ? value : StringUtils.EMPTY);
            conf.update(props);
        } catch (IOException e) {
            LOGGER.error("Could not set property", e);
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.millr.slick.services.OsgiService#setProperties(java.lang.String, java.util.Map)
     */
    public boolean setProperties(final String pid, final Map<String, Object> properties) {
        try {
            Configuration conf = configAdmin.getConfiguration(pid);

            @SuppressWarnings("unchecked")
            Dictionary<String, Object> props = conf.getProperties();

            if (props == null) {
                props = new Hashtable<String, Object>();
            }

            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                props.put(key, value != null ? value : StringUtils.EMPTY);
            }

            conf.update(props);
        } catch (IOException e) {
            LOGGER.error("Could not set property", e);
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.millr.slick.services.OsgiService#getStringProperty(java.lang.String, java.lang.String, java.lang.String)
     */
    public String getStringProperty(final String pid, final String property, final String defaultValue) {
        try {
            Configuration conf = configAdmin.getConfiguration(pid);

            @SuppressWarnings("unchecked")
            Dictionary<String, Object> props = conf.getProperties();

            if (props != null) {
                return PropertiesUtil.toString(props.get(property), defaultValue);
            }
        } catch (IOException e) {
            LOGGER.error("Could not get property", e);
        }

        return defaultValue;
    }

    /* (non-Javadoc)
     * @see org.millr.slick.services.OsgiService#getBooleanProperty(java.lang.String, java.lang.String, boolean)
     */
    public boolean getBooleanProperty(final String pid, final String property, final boolean defaultValue) {
        try {
            Configuration conf = configAdmin.getConfiguration(pid);

            @SuppressWarnings("unchecked")
            Dictionary<String, Object> props = conf.getProperties();

            if (props != null) {
                return PropertiesUtil.toBoolean(props.get(property), defaultValue);
            }
        } catch (IOException e) {
            LOGGER.error("Could not get property", e);
        }

        return defaultValue;
    }

    /* (non-Javadoc)
     * @see org.millr.slick.services.OsgiService#getLongProperty(java.lang.String, java.lang.String, java.lang.Long)
     */
    public Long getLongProperty(final String pid, final String property, final Long defaultValue) {
        long placeholder = -1L;
        long defaultTemp = defaultValue != null ? defaultValue : placeholder;

        try {
            Configuration conf = configAdmin.getConfiguration(pid);

            @SuppressWarnings("unchecked")
            Dictionary<String, Object> props = conf.getProperties();

            if (props != null) {
                long result = PropertiesUtil.toLong(props.get(property), defaultTemp);

                return result == placeholder ? null : result;
            }
        } catch (IOException e) {
            LOGGER.error("Could not get property", e);
        }

        return defaultValue;
    }

    /**
     * Wait for service.
     *
     * @param serviceImpl the service impl
     * @param timeout the timeout
     */
    @SuppressWarnings({ "unused", "rawtypes" })
    private void waitForService(Class serviceImpl, long timeout) {
        Class serviceInterface = serviceImpl.getInterfaces()[0];
        BundleContext bundleContext = FrameworkUtil.getBundle(serviceInterface).getBundleContext();
        ServiceReference factoryRef = bundleContext.getServiceReference(serviceInterface.getName());

        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, factoryRef, null);
        serviceTracker.open();

        try {
            serviceTracker.waitForService(timeout);
        } catch (InterruptedException e) {
            LOGGER.error("Could not get service", e);
        }

        serviceTracker.close();
    }
}