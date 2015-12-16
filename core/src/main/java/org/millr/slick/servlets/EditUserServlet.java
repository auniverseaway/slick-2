package org.millr.slick.servlets;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class EditUserServlet.
 */
@SlingServlet(
    resourceTypes = "sling/servlet/default",
    selectors = "user",
    extensions = "json",
    methods = {"GET", "POST"}
)
public class EditUserServlet extends SlingAllMethodsServlet {
	/**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EditUserServlet.class);
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant DEFAULT_ADMIN. */
	private static final String DEFAULT_ADMIN = "admin";
	
	@Reference
	private ResourceResolverFactory resolverFactory;
	
	/* (non-Javadoc)
	 * @see org.apache.sling.api.servlets.SlingSafeMethodsServlet#doGet(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("*** In doGet ***");
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.sling.api.servlets.SlingAllMethodsServlet#doPost(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)
	 */
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("*** In doPost ***");
		
		final String method = request.getParameter("method");	
		
	}

	
	
}