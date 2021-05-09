package com.bsuir.web.controllers;

import com.bsuir.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class UsersControllers {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String adminPage(Model model)
    {
        return "admin";
    }

    @GetMapping("/user")
    public String userPage()
    {
        return  "user";
    }

    @GetMapping("/expert")
    public String expertPage()
    {
        return "expert";
    }
}
