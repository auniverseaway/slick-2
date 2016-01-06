package org.millr.slick.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

@Model(adaptables=SlingHttpServletRequest.class)
public class FormatDate {
	
	@Inject
	private String dateFormat;
	
	@Inject
	private Calendar date;
	
	public String formattedValue;
	
	@PostConstruct
	protected void init() {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		// TODO: Pull timezone from OSGI config.
		formatter.setTimeZone(TimeZone.getTimeZone("America/Denver"));
	    formattedValue = formatter.format(date.getTime());
	}
}