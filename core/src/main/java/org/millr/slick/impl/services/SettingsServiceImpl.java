package org.millr.slick.impl.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.millr.slick.SlickConstants;
import org.millr.slick.services.SettingsService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component(name = "Slick Blog Settings",metatype=false)
public class SettingsServiceImpl implements SettingsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);
	
	private Map<String, Object> settings = new ConcurrentHashMap<>();
	
	@Reference
    private ResourceResolverFactory resourceResolverFactory = null;
	
	@Override
	public boolean setProperties(Map<String, Object> properties) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getBlogName() {
        return PropertiesUtil.toString(settings.get(SettingsService.BLOG_NAME), SlickConstants.DEFAULT_BLOG_NAME);
    }

	@Override
    public boolean setBlogName(final String name) {
        return setProperties(new HashMap<String, Object>() {{
            put(BLOG_NAME, name);
        }});
    }
	
	@Activate
    protected void activate(ComponentContext componentContext) {
        ResourceResolver resolver = null;
        try {
            resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            Resource settingsResource = resolver.getResource(SlickConstants.SETTINGS_PATH);
            if (settingsResource != null) {
                settings.clear();
                settings.putAll(settingsResource.adaptTo(ValueMap.class));
            }
        } catch (LoginException e) {
            LOGGER.error("Unable to read Publick settings.", e);
        } finally {
            if (resolver != null) {
                resolver.close();
            }
        }
    }

    private Resource getConfigurationResource() {
        ResourceResolver resolver;
        Resource settingsResource = null;
        try {
            resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            settingsResource = resolver.getResource(SlickConstants.SETTINGS_PATH);
        } catch (LoginException e) {
            LOGGER.error("Unable to read Publick settings.", e);
        }
        return settingsResource;
    }
	
}