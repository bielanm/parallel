package com.romanb;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    private static final String fromEmail = "mykhailo.bielan@itrexgroup.com";
    private static final String password = "20170321-1";

    private final String source;
    private final Properties properties;

    public EmailService(String source, Properties properties) {
        this.source = source;
        this.properties = properties;
    }

    public void sendEmail(String email, String subject, String message) throws IOException {

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        EmailUtil.sendEmail(session, source, email, subject, message);

    }

}
