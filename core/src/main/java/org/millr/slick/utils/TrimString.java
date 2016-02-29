package org.millr.slick.utils;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrimString {
	
	public String trimmedString;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrimString.class);
	
	public TrimString(String input, int length, boolean soft) {
		
		LOGGER.info(">>>> Trimming String");
		
		// Replace basic HTML. Will break if HTML is malformed.
		String contentString = input.replaceAll("<[^>]*>", "");
		
		if(contentString == null || contentString.trim().isEmpty()){
			LOGGER.info("String is empty");
	        trimmedString = contentString;
	    }

	    try {
			StringBuffer sb = new StringBuffer(contentString);
			
			int desiredLength = length;
			int endIndex = sb.indexOf(" ",desiredLength);
			
			//If soft, remove three characters to make room for elipsis.
			if(soft){
				desiredLength = length - 3;
				endIndex = sb.indexOf(" ",desiredLength);
				contentString = escapeHtml(sb.insert(endIndex,"...").substring(0, endIndex+3));
			} else {
				contentString = escapeHtml(sb.substring(0, endIndex));
			}
		} catch (Exception e) {
			LOGGER.error("Exception: " + e.getMessage(), e);
		}
	    trimmedString = contentString;
	}
}