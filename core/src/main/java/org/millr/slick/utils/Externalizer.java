package org.millr.slick.utils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

public class Externalizer extends WCMUse {
	
	private Resource resource;
	private SlingHttpServletRequest request;

	private String scheme;
	
	private String serverName;
	
	private int serverPort;
	
	private String fullDomain;
	

	@Override
    public void activate() {
		this.request = getRequest();
		this.resource = getResource();
        
        this.scheme = request.getScheme();
        this.serverName = request.getServerName();
        this.serverPort = request.getServerPort();
	}
	
	public String getScheme() {
		return scheme;
	}
	
	public String getFullDomain() {
		StringBuilder url = new StringBuilder();
	    url.append(scheme).append("://").append(serverName);
	    if (serverPort != 80 && serverPort != 443) {
	        url.append(":").append(serverPort);
	    }
	    return url.toString();
	}
	
	
//	public static String getURL(HttpServletRequest req) {
//
//	    String scheme = req.getScheme();             // http
//	    String serverName = req.getServerName();     // hostname.com
//	    int serverPort = req.getServerPort();        // 80
//	    String contextPath = req.getContextPath();   // /mywebapp
//	    String servletPath = req.getServletPath();   // /servlet/MyServlet
//	    String pathInfo = req.getPathInfo();         // /a/b;c=123
//	    String queryString = req.getQueryString();          // d=789
//
//	    // Reconstruct original requesting URL
//	    StringBuilder url = new StringBuilder();
//	    url.append(scheme).append("://").append(serverName);
//
//	    if (serverPort != 80 && serverPort != 443) {
//	        url.append(":").append(serverPort);
//	    }
//
//	    url.append(contextPath).append(servletPath);
//
//	    if (pathInfo != null) {
//	        url.append(pathInfo);
//	    }
//	    if (queryString != null) {
//	        url.append("?").append(queryString);
//	    }
//	    return url.toString();
//	}
}
