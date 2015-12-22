package org.millr.slick.components.common;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.millr.slick.services.SettingsService;
import org.millr.slick.utils.WCMUse;

/**
 * Sightly component to get System Settings such as blog name.
 */
public class Settings extends WCMUse {

    /** The Sling Script Helper to get the OSGi service. */
    private SlingScriptHelper scriptHelper;

    /** The current resource. */
    private Resource resource;

    private String blogName;

    private String temporaryDirectory;

    private boolean extensionlessUrls;

    @Override
    public void activate() {
        scriptHelper = getSlingScriptHelper();
        resource = getResource();

        SettingsService settingsService = scriptHelper.getService(SettingsService.class);

        if (settingsService != null) {
            blogName = settingsService.getBlogName();
            extensionlessUrls = settingsService.getExtensionlessUrls();
            temporaryDirectory = settingsService.getTemporaryDirectory();
        }
    }

    /**
     * Get the name of the blog.
     *
     * @return The name of the blog.
     */
    public String getBlogName() {
        return blogName;
    }

    /**
     * Get the temporary directory.
     *
     * @return The temporary directory.
     */
    public String getTemporaryDirectory() {
        return temporaryDirectory;
    }

    /**
     * Get the setting for extensionless URLs.
     *
     * @return The setting for extensionless URLs.
     */
    public boolean getExtensionlessUrls() {
        return extensionlessUrls;
    }
}