package com.bielanm.expresslike;

import java.util.Map;

public interface TemplateEngine {

    String handle(String html, Map<String, String> params);

}
