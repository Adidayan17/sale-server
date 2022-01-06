package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.UserObject;
import com.dev.utils.MessagesHandler;
import com.dev.utils.Utils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;


@RestController
public class TestController {


    @Autowired
    private Persist persist;
    private MessagesHandler messagesHandler;

    @PostConstruct
    private void init () {

    }


//    @RequestMapping(value = "/did-like")
//    public boolean   didLike (@RequestParam String token ,  int postId) {
//        return persist.didLike(token,postId);
//    }










}
