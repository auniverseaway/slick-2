package org.millr.slick.servlets.comment;

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

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.tika.io.IOUtils;
import org.millr.slick.services.CommentService;
import org.millr.slick.services.CurrentUserService;
import org.millr.slick.services.DispatcherService;
import org.millr.slick.services.UiMessagingService;
import org.millr.slick.utils.Externalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.CharEncoding;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "edit.comment",
        extensions = "json",
        methods = "POST"
    )
public class EditComment extends SlingAllMethodsServlet {
    
    private static final long serialVersionUID = -1150092606771566762L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EditComment.class);
    
    @Reference
    private CurrentUserService currentUserService;
    
    @Reference
    private UiMessagingService uiMessagingService;
    
    @Reference
    private CommentService commentService;
    
    @Reference 
    private org.millr.slick.services.settings.CommentService commentSettingsService;
    
    @Reference
    DispatcherService dispatcherService;
    
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
    	request.setCharacterEncoding(CharEncoding.UTF_8);
    		// response.setContentType("text/html; charset=UTF-8");
    		response.setCharacterEncoding(CharEncoding.UTF_8);
    		LOGGER.info(">>>> Entering doPost");
        
        // Get our parent resource
        Resource postResource = request.getResource();
        String postPath = request.getResource().getPath();
        
        // Detect a logged in user
        String authorId = currentUserService.getUserId(request.getResourceResolver());
        LOGGER.info("AUTHOR ID: " + authorId);
        
        boolean captchaValid = false;
        if(!authorId.equals("anonymous")) {
            captchaValid = true;
        } else {
            String remoteIp = request.getRemoteAddr();
            LOGGER.info(">>EditComment validateCaptcha " + remoteIp);
            captchaValid = validateCaptcha(request.getParameter("g-recaptcha-response"), remoteIp);
        }
        
        
        int responseCode;
        String responseType;
        String responseMessage;
        JSONObject responseContent = new JSONObject();
        
        String author = request.getParameter("author");
        String comment = request.getParameter("comment");
        
        
        
        // Replace basic HTML. Will break if HTML is malformed.
        comment = comment.replaceAll("<[^>]*>", "");
        
        if (captchaValid && StringUtils.isNotEmpty(comment)) {
            // Build our comment
            Map<String,Object> commentProperties = new HashMap<String,Object>();
            
            if(StringUtils.isEmpty(author)) {
                author = "anonymous";
            }
            
            commentProperties.put("comment", comment);
            commentProperties.put("author", author);
            if(!authorId.equals("anonymous")) {
                commentProperties.put("authorId", authorId);
                commentProperties.put("status", "approved");
            } else {
                commentProperties.put("status", commentSettingsService.getCommentsDefaultStatus());
            }
            commentProperties.put(JcrConstants.JCR_PRIMARYTYPE, "slick:comment");
            
            // Create our comment
            Resource commentResource = commentService.createComment(postResource, commentProperties);
            
            flushDispatch(request, postPath + ".list.comments.json");
            
            responseCode = 200;
            responseType = "success";
            responseMessage = "success";
            try {
                responseContent.put("name", commentResource.getName());
                responseContent.put("path", commentResource.getPath());
                responseContent.put("comment", commentProperties.get("comment"));
                responseContent.put("author", commentProperties.get("author"));
                responseContent.put("status", commentProperties.get("status"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            responseCode = 500;
            responseType = "error";
            responseMessage = "error";
        }
        uiMessagingService.sendResponse(response, responseCode, responseType, responseMessage, responseContent);
    }
    
    private Boolean validateCaptcha(String captcha, String remoteIp) {
        Boolean isValidCaptcha = false;
        
        // Setup captcha request values
        HttpURLConnection urlConn = null;
        BufferedReader reader = null;
        String charset = "UTF-8";
        String secret = commentSettingsService.getCommentsReCapchtaSecretKey();
        String query = String.format("secret=%s&response=%s", secret, captcha);

        try {
            final URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.setReadTimeout(15*1000);
            
            try (OutputStream output = urlConn.getOutputStream()) {
                output.write(query.getBytes(charset));
            }
            InputStream response = urlConn.getInputStream();
            LOGGER.info("*** RESPONSE MESSAGE *** ");
            String responseString = IOUtils.toString(response, charset);
            LOGGER.info(responseString);
            JSONObject responseObject = new JSONObject(responseString);
            isValidCaptcha = (Boolean) responseObject.get("success");
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
        return isValidCaptcha;
    }
    
    private void flushDispatch(SlingHttpServletRequest request, String path) {
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        dispatcherService.flushContent(currentDomain, path);
    }
}
