package com.romanb.expresslike;

import java.util.Map;

public interface TemplateEngine {

    String handle(String html, Map<String, String> params);

}
