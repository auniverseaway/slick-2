package org.millr.slick.impl.services;

import java.util.Map;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.millr.slick.services.SettingsService;
import org.millr.slick.services.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Constants;

@Service(value = SettingsService.class)
@Component(metatype = true,
           immediate = true,
           name = "org.millr.slick",
           label = "Slick Configuration",
           description = "General blog settings.")
@Properties({
    @Property(name = SettingsServiceImpl.SYSTEM_BLOG_NAME,
              value = SettingsServiceImpl.BLOG_NAME_DEFAULT_VALUE,
              label = "Blog Name",
              description = "The blog name is used throughout the application."),
    @Property(name = SettingsServiceImpl.SYSTEM_EXTENSIONLESS_URLS,
              boolValue = SettingsServiceImpl.EXTENSIONLESS_URLS_DEFAULT_VALUE,
              label = "Extentionless URLs",
              description = "Enabling extenionless URLs alters links written by the blog engine. "),
    @Property(name = Constants.SERVICE_DESCRIPTION,
              value = "General blog engine system settings."),
    @Property(name = Constants.SERVICE_VENDOR,
              value = "Slick")
})
public class SettingsServiceImpl implements SettingsService {

    /** Service to get and set OSGi properties. */
    @Reference
    private OsgiService osgiService;

    /** The logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);

    /** PID of the current OSGi component */
    private static final String COMPONENT_PID = "org.millr.slick";

    /** Default value for the blog name */
    public static final String BLOG_NAME_DEFAULT_VALUE = "Slick Blogging Engine";

    /** Default value for extensionless URLs */
    public static final boolean EXTENSIONLESS_URLS_DEFAULT_VALUE = false;

    /** Service activation */
    @Activate
    protected void activate(Map<String, Object> properties) {
    }

    /**
     * Set multiple properties for the System Settings service.
     *
     * This is useful for setting multiple properties as the same
     * time in that the OSGi component will only be updated once
     * and thus reset only once.
     *
     * @param properties A map of properties to set.
     * @return true if save was successful.
     */
    public boolean setProperties(final Map<String, Object> properties) {
        return osgiService.setProperties(COMPONENT_PID, properties);
    }

    /**
     * Get the name of the blog.
     *
     * @return The name of the blog.
     */
    public String getBlogName() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_BLOG_NAME, BLOG_NAME_DEFAULT_VALUE);
    }

    /**
     * Set the name of the blog.
     *
     * @param name The name of the blog.
     * @return true if the save was successful.
     */
    public boolean setBlogName(final String name) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_BLOG_NAME, name);
    }

    /**
     * Get the setting for extensionless URLs.
     *
     * @return The setting for extensionless URLS.
     */
    public boolean getExtensionlessUrls() {
        return osgiService.getBooleanProperty(COMPONENT_PID, SYSTEM_EXTENSIONLESS_URLS, EXTENSIONLESS_URLS_DEFAULT_VALUE);
    }

    /**
     * Set the value for extensionless URLs.
     *
     * @param value The setting for extensionless URLs.
     * @return true if the save was successful.
     */
    public boolean setExtensionlessUrls(final boolean value) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_EXTENSIONLESS_URLS, value);
    }

    /**
     * Get the setting for temporary directory.
     *
     * @return The setting for temporary directory.
     */
    public String getTemporaryDirectory() {
        return osgiService.getStringProperty(COMPONENT_PID, SYSTEM_TEMPORARY_DIRECTORY, null);
    }

    /**
     * Set the value for temporary directory.
     *
     * @param value The setting for the temporary directory.
     * @return true if the save was successful.
     */
    public boolean setTemporaryDirectory(final String directory) {
        return osgiService.setProperty(COMPONENT_PID, SYSTEM_TEMPORARY_DIRECTORY, directory);
    }

}