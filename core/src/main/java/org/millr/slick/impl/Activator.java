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
package org.millr.slick.impl;

import java.security.Principal;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.millr.slick.SlickConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
	
	/**
     * Logger instance to log and debug errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);
    
    @SuppressWarnings("deprecation")
	@Override
    public void start(BundleContext bundleContext) throws Exception {
    	
    	LOGGER.info(bundleContext.getBundle().getSymbolicName() + " started");
        ServiceReference resourceResolverFactoryReference = null;
        ResourceResolver resolver = null;
        try {
            resourceResolverFactoryReference = bundleContext.getServiceReference(ResourceResolverFactory.class.getName());
            ResourceResolverFactory resolverFactory = (ResourceResolverFactory) bundleContext.getService(resourceResolverFactoryReference);

            if (resolverFactory != null) {
                resolver = resolverFactory.getAdministrativeResourceResolver(null);
                createAuthorGroup(resolver);
                setPermissions(resolver);
            }
        } catch (LoginException e) {
            LOGGER.error("Could not login to repository", e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
            if (resourceResolverFactoryReference != null) {
                bundleContext.ungetService(resourceResolverFactoryReference);
            }
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    	LOGGER.info(bundleContext.getBundle().getSymbolicName() + " stopped");
    }
	
	private void createAuthorGroup(ResourceResolver resolver) {
		try {
			
            Session session = resolver.adaptTo(Session.class);
            if (session != null && session instanceof JackrabbitSession) {
            	
            	// Get our User Manager
                UserManager userManager = ((JackrabbitSession) session).getUserManager();
                ValueFactory valueFactory = session.getValueFactory();

                // Create the authors group if it doesn't exist already.
                Authorizable authors = userManager.getAuthorizable("authors");
                if (authors == null) {
                    authors = userManager.createGroup("authors");
                    authors.setProperty("displayName", valueFactory.createValue("Authors"));
                }
                
                // Create the default author if it doesn't exist already.
                Authorizable author = userManager.getAuthorizable("author");
                if(author == null) {
                	author = userManager.createUser("author", "letMeIn");
                	author.setProperty("firstName", valueFactory.createValue("Default"));
                	author.setProperty("lastName", valueFactory.createValue("Author"));
                	author.setProperty("email", valueFactory.createValue("info@slick.millr.org"));
                }  
                
                // Add author member to authors group
                ((Group) authors).addMember(author);
                
                // Save the session
                session.save();
            }
        } catch (RepositoryException e) {
            LOGGER.error("Could not get the session", e);
        }		
	}
    
    private void setPermissions(ResourceResolver resolver) {
    	JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);
        if (session != null) {
            try {
                Principal everyonePrincipal =  AccessControlUtils.getEveryonePrincipal(session);

                // Clear all permissions to publish, then selectively add back.
                AccessControlUtils.clear(session, SlickConstants.CONTENT_PATH);
                AccessControlUtils.denyAllToEveryone(session, SlickConstants.CONTENT_PATH);
                
                // Get our public publish node.
                Node publishNode = session.getNode(SlickConstants.PUBLISH_PATH);
                
                // Authors can do everything to the publish node.
                AccessControlUtils.allow(publishNode, "authors", Privilege.JCR_ALL);
                
                // Everyone can read the publish node.
                AccessControlUtils.allow(publishNode, everyonePrincipal.getName(), Privilege.JCR_READ);
                
                AccessControlUtils.clear(session, SlickConstants.AUTHOR_PATH);
                AccessControlUtils.denyAllToEveryone(session, SlickConstants.AUTHOR_PATH);
                AccessControlUtils.allow(session.getNode(SlickConstants.AUTHOR_PATH), "authors", Privilege.JCR_ALL);

                session.save();
            } catch (Exception e) {
                LOGGER.error("Unable to modify ACLs.", e);
            }
        }
    }

}