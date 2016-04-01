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
package org.millr.slick.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Author Class - Supply a resource and receive a user who created the content.
 * This assumes that jcr:createdBy is populated.
 */
@Model(adaptables = Resource.class)
public class Author
{
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Author.class);
    
    /** The user id. */
    @Inject @Optional @Named("jcr:createdBy")
    private String userId;
    
    /** The user. */
    private User user;
    
    /** The resource. */
    private Resource resource;
    
    /** The id. */
    private String id;
    
    /** The first name. */
    private String firstName;
    
    /** The last name. */
    private String lastName;
    
    /** The email. */
    private String email;

    /**
     * Instantiates a new author.
     *
     * @param resource the resource
     */
    public Author(final Resource resource) {
        this.resource = resource;
    }
    
    /**
     * Inits the Author Class.
     *
     * @throws AccessDeniedException the access denied exception
     * @throws UnsupportedRepositoryOperationException the unsupported repository operation exception
     * @throws RepositoryException the repository exception
     */
    @PostConstruct
    protected void init() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        JackrabbitSession js = (JackrabbitSession) session;
        final UserManager userManager = js.getUserManager();
        this.user = (User) userManager.getAuthorizable(userId);
        this.firstName = user.getProperty("firstName")[0].getString();
        this.lastName = user.getProperty("lastName")[0].getString();
        this.email = user.getProperty("email")[0].getString();
    }
    
    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }
}