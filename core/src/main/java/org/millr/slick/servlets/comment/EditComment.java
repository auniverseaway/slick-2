package org.millr.slick.servlets.comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
// import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import javax.jcr.Node;
// import javax.jcr.nodetype.NodeType;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.JcrConstants;
// import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
// import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
// import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
// import org.apache.tika.io.IOUtils;
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
    
    private Boolean validateCaptcha(String captcha, String remoteIp) { // remoteIP is optional, not yet implemented
        Boolean isValidCaptcha = false;
        
        // Setup captcha request values
        String charset = CharEncoding.UTF_8;
        String secret = commentSettingsService.getCommentsReCapchtaSecretKey();
        String query = String.format("secret=%s&response=%s", secret, captcha);
        // check in logfile that captcha really is different in each call (needs caching bypass)
        LOGGER.info("validateCaptcha: query " + query );

        try {
        	    URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true); // Triggers POST
            connection.setReadTimeout(15*1000);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
    	        OutputStreamWriter out = new OutputStreamWriter(
                                             connection.getOutputStream());
            out.write(query);
            out.close();
            BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                        connection.getInputStream()));
 // simple json response will look like this in case of *error*:
 // {
 //           "success": false,
 //           "error-codes": [
 //             "missing-input-response",
 //             "missing-input-secret"
 //           ]
 // } // hence simple regex processing is sufficient
            String decodedString;
            Pattern p_success = Pattern.compile("\"success\": true");
            Pattern p_error   = Pattern.compile("missing");
            Matcher m_success; 
            Matcher m_error; 

            while ((decodedString = in.readLine()) != null) {
            	  m_success = p_success.matcher(decodedString);
            	  m_error   = p_error.matcher  (decodedString);
            	  if ( m_success.find() ) { isValidCaptcha = true; }
            	  if ( m_error.find() ) {
                LOGGER.info("reCaptcha notValid response: " + decodedString );
                }
            }
            in.close();
            LOGGER.info("reCaptcha isValid executed - no exception!");
        }
        catch (Exception e)
        {
          e.printStackTrace();
          LOGGER.info("*** EXCEPTION *** ");
        }
        finally
        {
          LOGGER.info("*** FINALLY *** ");
        }
        return isValidCaptcha;
    }
    
    private void flushDispatch(SlingHttpServletRequest request, String path) {
        Externalizer external = request.adaptTo(Externalizer.class);
        String currentDomain = external.getDomain();
        dispatcherService.flushContent(currentDomain, path);
    }
}
