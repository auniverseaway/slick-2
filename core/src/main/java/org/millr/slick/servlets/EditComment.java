package org.millr.slick.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.tika.io.IOUtils;
import org.millr.slick.services.UiMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "comment",
        extensions = "json",
        methods = "POST"
    )
public class EditComment extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = -1150092606771566762L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EditComment.class);
    
    @Reference
    private UiMessagingService uiMessagingService;
    
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        LOGGER.info(">>>> Entering doPost");
        
        ResourceResolver resolver = request.getResourceResolver();
        
        // Get our parent resource
        String postPath = request.getPathInfo().replace(".comment.json", "");
        Resource postResource = resolver.getResource(postPath);
        Resource commentsResource = getCommentsResource(resolver, postResource);
        
        // Name our child
        String commentName = java.util.UUID.randomUUID().toString();
        String remoteIp = request.getRemoteAddr();
        Boolean captchaValid = validateCaptcha(request.getParameter("g-recaptcha-response"), remoteIp);
        
        Map<String,Object> properties = new HashMap<String,Object>();
        properties.put("comment", request.getParameter("comment"));
        properties.put("author", request.getParameter("author"));
        properties.put("status", "approved");
        properties.put(JcrConstants.JCR_PRIMARYTYPE, "slick:comment");
        
        Resource commentResource = resolver.create(commentsResource, commentName, properties);
        setMixin(commentResource, NodeType.MIX_CREATED);
        resolver.commit();
        resolver.close();
        
        JSONObject responseMessage = new JSONObject();
        try {
            responseMessage.put("comment", properties.get("comment"));
            responseMessage.put("path", postPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
        uiMessagingService.sendResponse(response, 200, "success", "success", responseMessage);
    }
    
    private Boolean validateCaptcha(String captcha, String remoteIp) {
        HttpURLConnection urlConn = null;
        BufferedReader reader = null;
        
        LOGGER.info("*** CAPTCHA *** " + captcha);
        LOGGER.info("*** IP *** " + remoteIp);
        
        String charset = "UTF-8";
        String secret = "6LdTTiYTAAAAAOpum_TRxlqYXq6QTqhBUpWAQMiU";
        
        String query = String.format("secret=%s&response=%s", secret, captcha);

        try {
            final URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            //urlConn.setAllowUserInteraction(false);
            urlConn.setDoOutput(true);
            urlConn.setReadTimeout(15*1000);
            
            try (OutputStream output = urlConn.getOutputStream()) {
                output.write(query.getBytes(charset));
            }
            InputStream response = urlConn.getInputStream();
            LOGGER.info("*** RESPONSE MESSAGE *** ");
            String responseString = IOUtils.toString(response, charset);
            LOGGER.info(responseString);
            IOUtils.closeQuietly(response);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          LOGGER.info("*** EXCEPTION *** ");
        }
        finally
        {
          LOGGER.info("*** FINALLY *** ");
          // close the reader; this can throw an exception too, so
          // wrap it in another try/catch block.
          if (reader != null)
          {
            try
            {
              reader.close();
            }
            catch (IOException ioe)
            {
              ioe.printStackTrace();
            }
          }
        }
        return true;
    }

    private Resource getCommentsResource(ResourceResolver resolver, Resource postResource) {
        Resource commentsResource = postResource.getChild("comments");
        Map<String,Object> properties = new HashMap<String,Object>();
        properties.put("jcr:primaryType", "sling:OrderedFolder");
        if(commentsResource == null) {
            try {
                commentsResource = resolver.create(postResource, "comments", properties);
                setMixin(commentsResource, NodeType.MIX_CREATED);
                resolver.commit();
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }
        
        return commentsResource;
    }
    
    private void setMixin(Resource post, String mixinName) {
        try {
            Node postNode = post.adaptTo(Node.class);
            postNode.addMixin(mixinName);
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }
}