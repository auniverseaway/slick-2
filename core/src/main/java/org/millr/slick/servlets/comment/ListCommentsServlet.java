package org.millr.slick.servlets.comment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.millr.slick.services.CommentService;
import org.millr.slick.services.UiMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "list.comments",
        extensions = "json",
        methods = "GET"
    )
public class ListCommentsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -5513977995823147919L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommentsServlet.class);
    
    @Reference
    private UiMessagingService uiMessagingService;
    
    @Reference
    private CommentService commentService;
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        Resource postResource = request.getResource();
        
        int responseCode;
        String responseType;
        String responseMessage;
        JSONObject responseContent = new JSONObject();
        
        responseCode = 200;
        responseType = "success";
        responseMessage = "success";
        
        Iterator<Resource> comments = commentService.getComments(postResource.getName());
        JSONArray commentsArray = new JSONArray();
        int commentsCount = 0;
        while (comments.hasNext()){
            commentsCount++;
            Resource comment = comments.next();
            ValueMap commentProperties = comment.adaptTo(ValueMap.class);
            
            String created = formatDate((Calendar) commentProperties.get("jcr:created"));
            
            JSONObject commentContent = new JSONObject();
            try {
                commentContent.put("name", comment.getName());
                commentContent.put("author", commentProperties.get("author"));
                commentContent.put("comment", commentProperties.get("comment"));
                commentContent.put("created", created);
                commentsArray.put(commentContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            responseContent.put("count", commentsCount);
            responseContent.put("comments", commentsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        
        uiMessagingService.sendResponse(response, responseCode, responseType, responseMessage, responseContent);
        
    }
    
    private String formatDate(Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM, d yyyy");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date.getTime());
    }
}