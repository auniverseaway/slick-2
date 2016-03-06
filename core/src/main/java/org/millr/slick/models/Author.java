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


/**
 * The Class Author. Supply a resource and receive a user who created the content.
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