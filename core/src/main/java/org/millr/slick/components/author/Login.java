package org.millr.slick.components.author;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.models.annotations.Model;
import org.millr.slick.SlickConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class Login {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Login.class);

    /**
     * The request.
     */
    private SlingHttpServletRequest request;
    
    private ResourceResolver resolver;
    
    public String reason;
    
    public String loginRedirect;
    
    public Boolean isAdminDefault;
    
    /**
     * Constructor.
     */
    public Login(SlingHttpServletRequest request) {
        this.request = request;
        this.resolver = request.getResourceResolver();
    }
    
    public Boolean getIsAdminDefault() {
    	Boolean result = null;
    	
    	try {
    		
    		// Get our current resolver.
    		ResourceResolver resolver = request.getResourceResolver();
    		
    		// Adapt to an anonymous session and get the repo as anonymous.
    		JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);
        	Repository repository = session.getRepository();
        	
        	// Create a new session by logging into using the default admin username and password.
			Session adminSession = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
			adminSession.logout();
			
			result = true;
			
		} catch (RepositoryException e) {
			LOGGER.debug("Repo Exception: ", e);
			result = false;
		}
    	
    	return result;
    }
    
    /**
     * Determine where we are going. If we cannot, send to main author page.
     */
    public String getLoginRedirect() {
        String loginRedirect = request.getParameter("resource");
        if (StringUtils.isEmpty(loginRedirect)) {
            loginRedirect = SlickConstants.AUTHOR_PATH + SlickConstants.PAGE_EXTENSION;
        }
        return loginRedirect;
    }

    public String getReason() {
        return request.getParameter(AuthenticationHandler.FAILURE_REASON);
    }
}