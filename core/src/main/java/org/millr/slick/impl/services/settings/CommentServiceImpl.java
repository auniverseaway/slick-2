package org.millr.slick.impl.services.settings;

import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyOption;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.millr.slick.services.OsgiService;
import org.millr.slick.services.settings.CommentService;

@Service(value = CommentService.class)
@Component(metatype = true,
           immediate = true,
           name = "org.millr.slick.impl.services.settings.CommentServiceImpl",
           label = "Slick Comment Configuration",
           description = "Slick comment settings.")
@Properties({
    @Property(label = "Default Status",
              name  = CommentServiceImpl.SYSTEM_COMMENTS_DEFAULT_STATUS,
              value = CommentServiceImpl.SYSTEM_COMMENTS_DEFAULT_STATUS_DEFAULT_VALUE,
              description = "The default status when a comment is successfully posted.",
              options = {
                  @PropertyOption(name = "approved", value = "Approved"),
                  @PropertyOption(name = "moderated", value = "Hold for moderation")
              }),
    @Property(name  = CommentServiceImpl.SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY,
              value = CommentServiceImpl.SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY_DEFAULT_VALUE,
              label = "reCAPTCHA Site Key",
              description = "The reCAPTCHA site key. Register here: https://www.google.com/recaptcha/admin"),
    @Property(name  = CommentServiceImpl.SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY,
              value = CommentServiceImpl.SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY_DEFAULT_VALUE,
              label = "reCAPTCHA Secret Key",
              description = "The reCAPTCHA secret key. Register here: https://www.google.com/recaptcha/admin")
})
public class CommentServiceImpl implements CommentService {
    
    @Reference
    private OsgiService osgiService;

    /** PID of the current OSGi component */
    private static final String COMPONENT_PID = "org.millr.slick.impl.services.settings.CommentServiceImpl";
    
    public static final String SYSTEM_COMMENTS_DEFAULT_STATUS_DEFAULT_VALUE = "approved";
    
    public static final String SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY_DEFAULT_VALUE = "";
    
    public static final String SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY_DEFAULT_VALUE = "";

    @Override
    public boolean setProperties(Map<String, Object> commentProperties) {
        return osgiService.setProperties(COMPONENT_PID, commentProperties);
    }

    @Override
    public String getCommentsDefaultStatus() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_COMMENTS_DEFAULT_STATUS, SYSTEM_COMMENTS_DEFAULT_STATUS_DEFAULT_VALUE);
    }

    @Override
    public boolean setCommentsDefaultStatus(String defaultStatus) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_COMMENTS_DEFAULT_STATUS, defaultStatus);
    }

    @Override
    public String getCommentsReCapchtaSiteKey() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY, SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY_DEFAULT_VALUE);
    }

    @Override
    public boolean setCommentsReCapchtaSiteKey(String siteKey) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_COMMENTS_RECAPTCHA_SITE_KEY, siteKey);
    }

    @Override
    public String getCommentsReCapchtaSecretKey() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY, SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY_DEFAULT_VALUE);
    }

    @Override
    public boolean setCommentsReCapchtaSecretKey(String secretKey) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_COMMENTS_RECAPTCHA_SECRET_KEY, secretKey);
    }
    
}