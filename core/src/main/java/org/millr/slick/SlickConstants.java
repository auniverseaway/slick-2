package org.millr.slick;

public final class SlickConstants {

    /** Prevent class instantiation */
    private SlickConstants() {
    }

    /** Content Path **/
    public static final String CONTENT_PATH = "/content/slick";
    
    /** Author Path **/
    public static final String AUTHOR_PATH = CONTENT_PATH + "/author";
    
    /** Login Path **/
    public static final String LOGIN_PATH = CONTENT_PATH + "/login";
    
    /** Publish Path **/
    public static final String PUBLISH_PATH = CONTENT_PATH + "";
    
    public static final String POSTS_PATH = PUBLISH_PATH + "/posts";
    
    /** Pages Path **/
    public static final String PAGES_PATH = PUBLISH_PATH + "/pages";
    
    /** Media Path **/
    public static final String MEDIA_PATH = PUBLISH_PATH + "/media";
    
    public static final String SLICK_BASE_RESOURCE_TYPE = "slick";
    
    public static final String POST_RESOURCE_TYPE = "slick/publish/post";
    
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
    
    /** Slick Node Types **/
    /** Defined in ui.apps/../SLING-INF **/
    public static final String NODE_TYPE = "slick";
    
    /** Slick Post Type **/
    public static final String NODE_POST_TYPE = NODE_TYPE + ":page";
    
    public static final String PAGINATION_QUERY_STRING = "page";
    
    public static final Long PAGINATION_SIZE = 5L;
}