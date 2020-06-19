package me.guojiang.blogbackend.Controllers;

import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Services.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public JsonResult<Map<String, String>> sendEmail() {
        String to = "1059194307@qq.com";
        String subject = "test";
        String text = "this is a test email";
        var sendOk = emailService.sendEmail(to, subject, text);
        var ok = new JsonResult.Builder<Map<String, String>>()
                .status(200)
                .data(Map.of("isOk", "Ok"))
                .message("ok")
                .build();
        var no = new JsonResult.Builder<Map<String, String>>()
                .status(500)
                .data(Map.of("isOk", "NO"))
                .message("NO")
                .build();
        return sendOk ? ok : no;
    }
}
