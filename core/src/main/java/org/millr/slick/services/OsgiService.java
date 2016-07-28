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

/**
 * The Base OSGi Service API.
 */
public interface OsgiService {

    /**
     * Set a single property.
     *
     * @param pid the pid
     * @param property the property
     * @param value the value
     * @return true, if successful
     */
    boolean setProperty(final String pid, final String property, final Object value);

    /**
     * Set multiple properties.
     *
     * @param pid the pid
     * @param properties the properties
     * @return true, if successful
     */
    boolean setProperties(final String pid, final Map<String, Object> properties);

    /**
     * Get a string property.
     *
     * @param pid the pid
     * @param property the property
     * @param defaultValue the default value
     * @return the string property
     */
    String getStringProperty(final String pid, final String property, final String defaultValue);

    /**
     * Get a boolean property.
     *
     * @param pid the pid
     * @param property the property
     * @param defaultValue the default value
     * @return the boolean property
     */
    boolean getBooleanProperty(final String pid, final String property, final boolean defaultValue);

    /**
     * Get a long property.
     *
     * @param pid the pid
     * @param property the property
     * @param defaultValue the default value
     * @return the long property
     */
    Long getLongProperty(final String pid, final String property, final Long defaultValue);
}