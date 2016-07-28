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
package org.millr.slick.utils;

import org.apache.sling.api.SlingHttpServletRequest;
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
        this.scheme = request.getScheme();                // http
        this.serverName = request.getServerName();        // hostname.com
        this.serverPort = request.getServerPort();        // 80
        this.contextPath = request.getContextPath();      // /mywebapp
        this.servletPath = request.getServletPath();      // /servlet/MyServlet
        this.pathInfo = request.getPathInfo();            // /a/b;c=123
        this.queryString = request.getQueryString();      // d=789
        
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
        return url.replace("/content/slick/publish", "");
    }
    
}
