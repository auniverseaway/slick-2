package org.millr.slick.services.settings;

import java.util.Map;

public interface CommentService {
    
    public static final String SYSTEM_COMMENTS_DEFAULT_STATUS = "system.comments.defaultStatus";
    
    public static final String SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY = "system.comments.reCapchtaSiteKey";
    
    public static final String SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY = "system.comments.reCapchtaSecretKey";
    
    boolean setProperties(final Map<String, Object> commentProperties);
    
    String getCommentsDefaultStatus();
    
    boolean setCommentsDefaultStatus(final String defaultStatus);
    
    String getCommentsReCapchtaSiteKey();
    
    boolean setCommentsReCapchtaSiteKey(final String siteKey);
    
    String getCommentsReCapchtaSecretKey();
    
    boolean setCommentsReCapchtaSecretKey(final String secretKey);
}