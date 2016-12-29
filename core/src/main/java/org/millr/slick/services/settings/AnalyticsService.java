package org.millr.slick.services.settings;

import java.util.Map;

public interface AnalyticsService {
    
    public static final String SYSTEM_ANALYTICS_SERVICE_NAME = "system.analytics.serviceName";
    
    public static final String SYSTEM_ANALYTICS_HEAD_SCRIPT = "system.analytics.headScript";
    
    public static final String SYSTEM_ANALYTICS_FOOT_SCRIPT = "system.analytics.footScript";
    
    public static final String SYSTEM_ANALYTICS_REPORT_SUITE = "system.analytics.reportSuite";
    
    public static final String SYSTEM_ANALYTICS_TWITTER_USERNAME = "system.analytics.twitterUsername";
    
    public static final String SYSTEM_ANALYTICS_FACEBOOK_APPID = "system.analytics.facebookAppId";
    
    /**
     * Set all Analytics properties via Front-end
     * @param properties
     * @return boolean
     */
    boolean setProperties(final Map<String, Object> analyticsProperties);
    
    /**
     * Get the analytics service name.
     *
     * @return The analytics service name.
     */
    String getAnalyticsServiceName();
    
    /**
     * Set the analytics service name.
     *
     * @param script The analytics service name.
     * @return true if the save was successful.
     */
    boolean setAnalyticsServiceName(final String serviceName);
    
    /**
     * Get the analytics head script.
     *
     * @return The analytics head script.
     */
    String getAnalyticsHeadScript();
    
    /**
     * Set the analytics head script.
     *
     * @param script The analytics foot script.
     * @return true if the save was successful.
     */
    boolean setAnalyticsHeadScript(final String headScript);
    
    /**
     * Get the analytics foot script.
     *
     * @return The analytics head script.
     */
    String getAnalyticsFootScript();
    
    /**
     * Set the analytics foot script.
     *
     * @param script The analytics foot script.
     * @return true if the save was successful.
     */
    boolean setAnalyticsFootScript(final String footScript);
    
    /**
     * Get the analytics report suite.
     *
     * @return The analytics report suite.
     */
    String getAnalyticsReportSuite();
    
    /**
     * Set the analytics report suite.
     *
     * @param reportSuite The analytics report suite.
     * @return true if the save was successful.
     */
    boolean setAnalyticsReportSuite(final String reportSuite);
    
    /**
     * Get the Twitter user name.
     *
     * @return The Twitter user name.
     */
    String getAnalyticsTwitterUsername();
    
    /**
     * Set the Twitter user name.
     *
     * @param userName The Twitter user name.
     * @return true if the save was successful.
     */
    boolean setAnalyticsTwitterUsername(final String userName);
    
    /**
     * Get the Facebook App ID.
     *
     * @return The Facebook App ID.
     */
    String getAnalyticsFacebookAppId();
    
    /**
     * Set the Facebook App ID.
     *
     * @param facebookAppId The Facebook App ID.
     * @return true if the save was successful.
     */
    boolean setAnalyticsFacebookAppId(final String facebookAppId);
}