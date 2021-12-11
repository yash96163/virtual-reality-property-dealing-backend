package com.hashedin.virtualproperty.application.service;

import com.hashedin.virtualproperty.application.entities.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration freemarkerConfig;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    public void sendVerificationEmail(String to, String link) throws IOException, MessagingException, TemplateException {
        this.sendEmail(to, link, "Verify your email at Hash Homes", "email_verify.ftl");
        this.logger.info("VERIFICATION EMAIL SEND TO " + to);
    }

    public void sendPasswordResetEmail(String to, String link) throws IOException, MessagingException, TemplateException {
        this.sendEmail(to, link, "Password reset requested at Hash Homes", "email_reset.ftl");
        this.logger.info("PASSWORD RESET EMAIL SEND TO " + to);
    }

    public void sendUserViewedDetailEmail(String to, User user) throws MessagingException, TemplateException, IOException {
        this.logger.info("SENDING USER VIEWED PROFILE EMAIL TO " + to);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Template t = freemarkerConfig.getTemplate("detail_view.ftl");
        Map model = new HashMap();
        model.put("name", user.getName());
        model.put("email", user.getEmail());
        model.put("address", user.getAddress());
        model.put("mobile", user.getMobile());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject("Someone viewed your profile at HashHomes!");
        helper.setFrom("hashhomeshu@gmail.com");

        emailSender.send(message);
        this.logger.info("EMAIL SENT");
    }

    public void sendOwnerDetailEmail(String to, User owner) throws MessagingException, TemplateException, IOException {
        this.logger.info("SENDING OWNER DETAIL EMAIL TO " + to);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Template t = freemarkerConfig.getTemplate("owner_info.ftl");
        Map model = new HashMap();
        model.put("name", owner.getName());
        model.put("email", owner.getEmail());
        model.put("address", owner.getAddress());
        model.put("mobile", owner.getMobile());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject("Someone viewed your profile at HashHomes!");
        helper.setFrom("hashhomeshu@gmail.com");

        emailSender.send(message);
        this.logger.info("EMAIL SENT");
    }

    private void sendEmail(String to, String link, String subject, String templateName) throws MessagingException, IOException, TemplateException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Template t = freemarkerConfig.getTemplate(templateName);
        Map model = new HashMap();
        model.put("link", link);
        model.put("email", to);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom("hashhomeshu@gmail.com");

        emailSender.send(message);
    }

}