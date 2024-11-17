package com.devtam.userservice.controller;

import com.devtam.commonbase.dto.MailModel;
import com.devtam.commonbase.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @Autowired
    MailService mailService;

    @PostMapping("/send/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody MailModel mailModel) {
        mailService.sendMail(mail, mailModel);
        return "successful hehe";
    }
}
