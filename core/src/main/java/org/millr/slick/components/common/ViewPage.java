package org.millr.slick.components.common;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.millr.slick.models.Page;
import org.millr.slick.models.Author;
import org.millr.slick.utils.Externalizer;
import org.millr.slick.utils.WCMUse;

public class ViewPage extends WCMUse {
	
	private Resource resource;
	
	private SlingHttpServletRequest request;
	
	private Page page;
	
	private Author author;
	
	private Externalizer external;
	
	@Override
    public void activate() {
		this.request = getRequest();
		this.resource = getResource();
		this.page = resource.adaptTo(Page.class);
		this.author = resource.adaptTo(Author.class);
		this.external = request.adaptTo(Externalizer.class);
	}
	
	public Page getPage() {
		return page;
	}
	
	public Author getAuthor() {
		return author;
	}
	
	public Externalizer getExternal() {
		return external;
	}
}