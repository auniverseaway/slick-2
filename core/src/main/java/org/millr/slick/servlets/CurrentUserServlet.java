package org.millr.slick.servlets;

import java.io.IOException;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(paths="/bin/slick/getCurrentUser", methods="GET", metatype=false)
public class CurrentUserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 8769581589662680558L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserServlet.class);
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        ResourceResolver resolver = request.getResourceResolver();
        Session session = resolver.adaptTo(Session.class);
        JackrabbitSession jsSession = (JackrabbitSession) session;
        try {
			final UserManager userManager = jsSession.getUserManager();
			final User user = (User) userManager.getAuthorizable(session.getUserID());
			String firstName = user.getProperty("firstName")[0].getString();
		} catch (RepositoryException repositoryException) {
			
			repositoryException.printStackTrace();
		}
        
        
        String userId = session.getUserID();
        try {
            response.getWriter().write(userId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}