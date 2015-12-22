package org.millr.slick.impl.services;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.api.SlingRepository;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.CurrentUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrentUserServiceImpl.
 */
@Service
@Component
public class CurrentUserServiceImpl implements CurrentUserService {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserServiceImpl.class);
	
	/** The jackrabbit session. */
	private JackrabbitSession jackrabbitSession = null;
	
	/** The user. */
	private User user = null;
	
	/** The JCR session. */
    private Session session = null;

    /** The JCR Repository. */
    @Reference
    private SlingRepository repository;
	
	/* (non-Javadoc)
	 * @see org.millr.slick.services.CurrentUserService#getSession(org.apache.sling.api.resource.ResourceResolver)
	 */
	public void getSession(ResourceResolver resourceResolver) {
		jackrabbitSession = ((JackrabbitSession) resourceResolver.adaptTo(Session.class));
	}
	
	/* (non-Javadoc)
	 * @see org.millr.slick.services.CurrentUserService#getUser(org.apache.sling.api.resource.ResourceResolver)
	 */
	public void getUser(ResourceResolver resourceResolver) {
		
		getSession(resourceResolver);
		try {
            user = (User) jackrabbitSession.getUserManager().getAuthorizable(jackrabbitSession.getUserID());
		} catch (RepositoryException e) {
			LOGGER.error("Could not get user.", e);
		}
	}
	
	/**
	 * Gets the first name.
	 *
	 * @param resourceResolver the resource resolver
	 * @return the first name
	 */
	public String getFirstName(ResourceResolver resourceResolver) {
		
		// Get the user
		getUser(resourceResolver);
		String name = null;
		
		// Try to get the user's first name.
		try {
			name = user.getProperty("firstName")[0].toString();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
				
		return name;
	}
	
	public String getId(ResourceResolver resourceResolver) {
		
		// Get the user
		getUser(resourceResolver);
		String id = null;
		
		try {
			id = user.getID();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	/**
	 * Determine if user can author content.
	 *
	 * @param resourceResolver the resource resolver
	 * @return boolean
	 */
	public boolean getAuthorable(ResourceResolver resourceResolver) {
        boolean authorable = false;
        try {
        	getUser(resourceResolver);
            Group authors = (Group)jackrabbitSession.getUserManager().getAuthorizable(SlickConstants.AUTHOR_GROUP);
            authorable = user.isAdmin() || authors.isMember(user);
        } catch (RepositoryException e) {
            LOGGER.error("Couldn't get authorization.", e);
        }
        
        return authorable;
    }
}