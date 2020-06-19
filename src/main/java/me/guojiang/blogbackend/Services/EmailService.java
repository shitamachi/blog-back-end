package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${verify-email.validity-in-ms}")
    private String expTime;

    @Value("${verify-email.code-length}")
    private String codeLength;

    @Resource
    private JavaMailSender emailSender;

    private final Environment env;
    private final VerifyCodeUtil verifyCodeUtil;

    public EmailService(
            Environment env,
            VerifyCodeUtil verifyCodeUtil) {
        this.env = env;
        this.verifyCodeUtil = verifyCodeUtil;
    }


    public boolean sendEmail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setFrom(from);
        try {
            emailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("email send successful");
        return true;
    }

    @Async
    public CompletableFuture<Boolean> sendCodeAndSave(String username, String to) {
        var verifyCode = verifyCodeUtil.generateCodeAndSave(username, to, System.currentTimeMillis() + Long.parseLong(expTime), Integer.parseInt(codeLength));
        return CompletableFuture.completedFuture(sendCode(to, verifyCode));
    }

    private boolean sendCode(String to, String verifyCode) {
        return sendEmail(to, env.getProperty("verify-email.subject"),
                env.getProperty("verify-email.content") + " " + verifyCode);
    }

    public boolean verifyCode(String username, String email, String code) {
        return verifyCodeUtil.verifyCode(username, email, code);
    }
}
