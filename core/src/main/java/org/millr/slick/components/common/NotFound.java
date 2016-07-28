package org.millr.slick.components.common;

import javax.script.Bindings;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.scripting.sightly.pojo.Use;

public class NotFound implements Use {

    @Override
    public void init(Bindings bindings) {
        SlingHttpServletResponse response = (SlingHttpServletResponse) bindings.get(SlingBindings.RESPONSE);
        response.setStatus(404);
        response.setContentType("text/html");
    }
}