package com.romanb.expresslike;

import java.util.Map;

public class ExpresslikeTemplateEngine implements TemplateEngine {

    @Override
    public String handle(String html, Map<String, String> params) {
        for (Map.Entry<String, String> param: params.entrySet()) {
            html = html.replaceAll(param.getKey(), param.getValue());
        }
        return html;
    }
}
