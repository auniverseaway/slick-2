package org.millr.slick.services.settings;

import java.util.Map;

public interface AnalyticsService {
    
    public static final String SYSTEM_ANALYTICS_SERVICE_NAME = "system.analytics.serviceName";
    
    public static final String SYSTEM_ANALYTICS_HEAD_SCRIPT = "system.analytics.headScript";
    
    public static final String SYSTEM_ANALYTICS_FOOT_SCRIPT = "system.analytics.footScript";
    
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
}