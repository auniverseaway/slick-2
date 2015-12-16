package org.millr.slick;

public final class SlickConstants {

    /** Prevent class instantiation */
    private SlickConstants() {
    }

    /** Content Path **/
    public static final String CONTENT_PATH = "/content/slick";
    
    /** Author Path **/
    public static final String AUTHOR_PATH = CONTENT_PATH + "/author";
    
    /** Publish Path **/
    public static final String PUBLISH_PATH = CONTENT_PATH + "/publish";
    
    /** Apps Path **/
    public static final String APP_PATH = "/apps/slick";
    
    /** Etc Path **/
    public static final String ETC_PATH = "/etc/slick";
    
    /** Clientlib Path **/
    public static final String CLIENTLIB_PATH = ETC_PATH + "/designs";
    
    /** Page Extension **/
    public static final String PAGE_EXTENSION = ".html";
    
    /** Default Author Group **/
    public static final String AUTHOR_GROUP = "authors";
    
    /** Default Author Group Display Name **/
    public static final String AUTHOR_GROUP_DISPLAY_NAME = "Authors";
    
    /** Slick Servlet Path **/
    public static final String SERVLET_PATH = "/bin/slick";
    
    /** Slick Default Blog Values **/
    public static final String DEFAULT_BLOG_NAME = "Slick";
    
    /** Slick Default Blog Values **/
    public static final String SETTINGS_PATH = ETC_PATH + "/settings/slick";
}