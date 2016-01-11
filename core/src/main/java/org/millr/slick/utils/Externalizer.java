package org.millr.slick.utils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables=SlingHttpServletRequest.class)
public class Externalizer {
	
	private SlingHttpServletRequest request;

	private String scheme;
	
	private String serverName;
	
	private int serverPort;
	
	private String contextPath;
	
	private String servletPath;
	
	private String pathInfo;
	
	private String queryString;
	
	private String domain;
	
	private String url;
	
	public Externalizer(SlingHttpServletRequest request) {
		this.request = request;
		
		// Get all parts of the URL
		this.scheme = request.getScheme();				// http
        this.serverName = request.getServerName();		// hostname.com
        this.serverPort = request.getServerPort();		// 80
        this.contextPath = request.getContextPath();	// /mywebapp
	    this.servletPath = request.getServletPath();	// /servlet/MyServlet
	    this.pathInfo = request.getPathInfo();			// /a/b;c=123
	    this.queryString = request.getQueryString();	// d=789
	    
	    // Build the Domain
	    StringBuilder url = new StringBuilder();
	    url.append(scheme).append("://").append(serverName);
	    if (serverPort != 80 && serverPort != 443) {
	        url.append(":").append(serverPort);
	    }
	    this.domain = url.toString();
	    
	    // Build the rest of the URL
	    url.append(contextPath).append(servletPath);

	    if (pathInfo != null) {
	        url.append(pathInfo);
	    }
	    if (queryString != null) {
	        url.append("?").append(queryString);
	    }
	    this.url = url.toString();
	    
	}

	public String getDomain() {
		return domain;
	}
	
	public String getUrl() {
		return url;
	}
	
}
