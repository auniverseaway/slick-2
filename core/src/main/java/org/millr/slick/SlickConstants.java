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
package org.millr.slick;

/**
 * The <code>SlickConstants</code> interface provides some symbolic constants
 * for well known constant strings in Slick. Even though these constants will
 * never change, it is recommended that applications refer to the symbolic
 * constants instead of code the strings themselves.
 * <p>
 * This class is not intended to be extended or instantiated because
 * provides constants not intended to be overwritten.
 */
public final class SlickConstants {

    /** Prevent class instantiation */
    private SlickConstants() {
    }

    /** Content Path */
    public static final String CONTENT_PATH = "/content/slick";
    
    /** Author Path */
    public static final String AUTHOR_PATH = CONTENT_PATH + "/author";
    
    /** Login Path */
    public static final String LOGIN_PATH = CONTENT_PATH + "/login";

    /** Search Path */
    public static final String SEARCH_PATH = CONTENT_PATH + "/search";
    
    /** Feed Path */
    public static final String FEED_PATH = CONTENT_PATH + "/feed";
    
    /** Publish Path */
    public static final String PUBLISH_PATH = CONTENT_PATH + "/publish";
    
    /** Posts Path */
    public static final String POSTS_PATH = PUBLISH_PATH + "/posts";
    
    /** Pages Path */
    public static final String PAGES_PATH = PUBLISH_PATH + "/pages";
    
    /** Draft Path */
    public static final String DRAFT_PATH = PUBLISH_PATH + "/draft";
    
    /** Media Path */
    public static final String MEDIA_PATH = PUBLISH_PATH + "/media";
    
    /** Comments Path */
    public static final String COMMENTS_PATH = PUBLISH_PATH + "/comments";
    
    /** Favicon Path */
    public static final String FAVICON_PATH = PUBLISH_PATH + "/favicon.ico";
    
    /** Base ResourceType */
    public static final String SLICK_BASE_RESOURCE_TYPE = "slick";
    
    /** Publish ResourceType */
    public static final String PUBLISH_RESOURCE_TYPE = SLICK_BASE_RESOURCE_TYPE + "/publish";
    
    /** Post Detail ResourceType */
    public static final String POST_RESOURCE_TYPE = SLICK_BASE_RESOURCE_TYPE + PUBLISH_RESOURCE_TYPE + "/post/detail";
    
    /** Apps Path */
    public static final String APP_PATH = "/apps/slick";
    
    /** Etc Path */
    public static final String ETC_PATH = "/etc/slick";
    
    /** Clientlib Path */
    public static final String CLIENTLIB_PATH = ETC_PATH + "/designs";
    
    /** Page Extension */
    public static final String PAGE_EXTENSION = ".html";
    
    /** Default Author Group */
    public static final String AUTHOR_GROUP = "authors";
    
    /** Default Author Group Display Name */
    public static final String AUTHOR_GROUP_DISPLAY_NAME = "Authors";
    
    /** Slick Servlet Path */
    public static final String SERVLET_PATH = "/bin/slick";
    
    /** Slick Default Blog Values */
    public static final String DEFAULT_BLOG_NAME = "Slick";
    
    /** Slick Default Blog Values */
    public static final String SETTINGS_PATH = ETC_PATH + "/settings/slick";
    
    /** Slick Node Types */
    /** Defined in ui.apps/../SLING-INF **/
    public static final String NODE_TYPE = "slick";
    
    /** Slick Post Type */
    public static final String NODE_POST_TYPE = NODE_TYPE + ":page";
    
    public static final String NODE_COMMENT_TYPE = NODE_TYPE + ":comment";
    
    public static final String PAGINATION_QUERY_STRING = "page";
    
    public static final Long PAGINATION_SIZE = 5L;
}