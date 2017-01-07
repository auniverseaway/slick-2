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
package org.millr.slick.utils;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trim String Utility
 * Receive a string and trim it to a desired length. Remove basic HTML if needed.
 */
public class TrimString {
    
    public String trimmedString;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TrimString.class);
    
    public TrimString(String input, int length, boolean soft) {
        
        LOGGER.info(">>>> Trimming String");
        
        // Replace basic HTML. Will break if HTML is malformed.
        String contentString = input.replaceAll("<[^>]*>", "");
        
        if(contentString == null || contentString.trim().isEmpty()){
            trimmedString = contentString;
        }

        StringBuffer sb = new StringBuffer(contentString);
        int actualLength = length - 3;
        if(sb.length() > actualLength){
            // -3 because we add 3 dots at the end. Returned string length has to be length including the dots.
            if(!soft)
                trimmedString = escapeHtml(sb.insert(actualLength, "...").substring(0, actualLength + 3));
            else {
                int endIndex = sb.indexOf(" ",actualLength);
                trimmedString = escapeHtml(sb.insert(endIndex,"...").substring(0, endIndex + 3));
            }
        } else {
            trimmedString = escapeHtml(contentString);
        }
    }
}