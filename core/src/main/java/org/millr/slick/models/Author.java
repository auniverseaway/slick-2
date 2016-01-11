package org.millr.slick.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class Author
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Author.class);
	
	@Inject @Optional @Named("jcr:createdBy")
	private String userId;
	
	private User user;
	
	private Resource resource;
	
	private String id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;

	public Author(final Resource resource) {
        this.resource = resource;
    }
	
	@PostConstruct
	protected void init() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
		LOGGER.info("User ID: " + userId);
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
    	JackrabbitSession js = (JackrabbitSession) session;
    	final UserManager userManager = js.getUserManager();
    	this.user = (User) userManager.getAuthorizable(userId);
    	this.firstName = user.getProperty("firstName")[0].getString();
    	this.lastName = user.getProperty("lastName")[0].getString();
    	this.email = user.getProperty("email")[0].getString();
	}
	
	public User getUser() {
		return user;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
}