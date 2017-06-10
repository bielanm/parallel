package com.romanb;


import com.romanb.expresslike.ExpresslikeApp;
import com.romanb.expresslike.ExpresslikeRequestHandlerImpl;
import com.romanb.expresslike.ExpresslikeTemplateEngine;
import com.romanb.expresslike.TemplateEngine;
import com.romanb.net.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class FifthSolution {


    private static final String EMAILS_PATH = "src/main/resources/emails.txt";
    private static final String helloWorld =
            "<html>"+
                "<header>" +
                    "<title>Email list</title>" +
                "</header>"+
                "<body>"+
                    "emails" +
                "</body>"+
            "</html>";

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        EmailService emailService = new EmailService("useless_development", properties);

        TemplateEngine engine = new ExpresslikeTemplateEngine();
        ExpresslikeApp app = ExpresslikeApp.listen(8181, new ExpresslikeRequestHandlerImpl());
        app.addGet("/", (httpRequest, httpResponse) -> {
            Map<String, String> params = new HashMap<String, String>();
            try {
                params.put("emails", Files.readAllLines(Paths.get(EMAILS_PATH))
                        .stream()
                        .map(line -> "<p>" + line + "</p>")
                        .collect(Collectors.joining("\n")));
                httpResponse.sendHtml(app.getTemplateEngine().handle(helloWorld, params));
                httpResponse.end();
            } catch (IOException e) {
                httpResponse.sendBody("Error");
                httpResponse.setStatus(HttpStatus.BAD_REQUEST);
            }
           })
           .addStatic("templates")
           .addPost("/send", (httpRequest, httpResponse) -> {
               Map<String, String> body = httpRequest.getBody();
               String email = body.get("email");
               String message = body.get("message");
               if(email != null && message != null) {
                   try {
                       if(Files.readAllLines(Paths.get(EMAILS_PATH)).stream().filter(email::equals).findAny().isPresent()) {
                           emailService.sendEmail(email, "Lab #6 email" ,message);
                       } else {
                           httpResponse.setStatus(HttpStatus.BAD_REQUEST);
                           httpResponse.sendBody("Email list doesn't contain ths email...");
                       }
                   } catch (IOException e) {
                       httpResponse.setStatus(HttpStatus.ERROR);
                       httpResponse.sendBody("Smth was wrong...");
                   }
               } else {
                   httpResponse.setStatus(HttpStatus.BAD_REQUEST);
                   httpResponse.sendBody("Specify \"message\" and \"email\" fields...");
               }

               httpResponse.end();
           })
           .addElse((httpRequest, httpResponse) -> {
               httpResponse.redirect("/");
           });
        app.start();
    }

}
