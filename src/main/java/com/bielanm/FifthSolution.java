package com.bielanm;


import com.bielanm.expresslike.ExpresslikeApp;
import com.bielanm.expresslike.ExpresslikeRequestHandlerImpl;
import com.bielanm.expresslike.ExpresslikeTemplateEngine;
import com.bielanm.expresslike.TemplateEngine;
import com.bielanm.util.fifthlab.ShakespeareRandomizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FifthSolution {


    private static final String SHAKESPEARE_FILE_PATH = "src/main/resources/shakespeare_sonets.txt";
    private static final String helloWorld =
            "<html>"+
                "<header>" +
                    "<title>Shakespeare</title>" +
                "</header>"+
                "<body>"+
                    "sonet" +
                "</body>"+
            "</html>";

    public static void main(String[] args) throws IOException {
        ShakespeareRandomizer shakespeareRandomizer = ShakespeareRandomizer.newInstance(SHAKESPEARE_FILE_PATH);

        TemplateEngine engine = new ExpresslikeTemplateEngine();
        ExpresslikeApp app = ExpresslikeApp.listen(8181, new ExpresslikeRequestHandlerImpl());
        app.addGet("/", (httpRequest, httpResponse) -> {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sonet", shakespeareRandomizer.getRandom().stream().map(line -> "<p>" + line + "</p>").collect(Collectors.joining()));
                    httpResponse.sendHtml(app.getTemplateEngine().handle(helloWorld, params));
                    httpResponse.end();
           })
           .addStatic("templates")
           .addElse((httpRequest, httpResponse) -> {
               httpResponse.redirect("/");
           });
        app.start();
    }

}
