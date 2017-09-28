package org.millr.slick.servlets.comment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.millr.slick.services.CommentService;
import org.millr.slick.services.UiMessagingService;

@SlingServlet(
        resourceTypes = "sling/servlet/default",
        selectors = "list.comments",
        extensions = "json",
        methods = "GET"
    )
public class ListCommentsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -5513977995823147919L;
    
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
        JsonObjectBuilder responseContentBuilder = Json.createObjectBuilder();
        
        responseCode = 200;
        responseType = "success";
        responseMessage = "success";
        
        NodeIterator comments = commentService.getItemPublicComments(postResource, "approved");
        JsonArrayBuilder commentsArrayBuilder = Json.createArrayBuilder();
        int commentsCount = 0;
        while (comments.hasNext()){
            commentsCount++;
            Node comment = (Node) comments.next();
            
            String created = null;
            try {
                created = formatDate((Calendar) comment.getProperty("jcr:created").getDate());
            } catch (PathNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (RepositoryException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            JsonObjectBuilder commentBuilder = Json.createObjectBuilder();
            try {
                commentBuilder.add("name", comment.getName());
                commentBuilder.add("author", comment.getProperty("author").getString());
                commentBuilder.add("comment", comment.getProperty("comment").getString());
                commentBuilder.add("created", created);
                JsonObject commentContent = commentBuilder.build();
                commentsArrayBuilder.add(commentContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            responseContentBuilder.add("count", commentsCount);
            responseContentBuilder.add("comments", commentsArrayBuilder.build());
        } catch (JsonException e) {
            e.printStackTrace();
        }

        uiMessagingService.sendResponse(response, responseCode, responseType, responseMessage, responseContentBuilder.build());
        
    }
    
    private String formatDate(Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM, d yyyy");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date.getTime());
    }
}