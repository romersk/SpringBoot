package com.bsuir.web.controllers;

import com.bsuir.web.model.Users;
import com.bsuir.web.repository.UserRepository;
import com.bsuir.web.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String viewHomePage(Model model)
    {
        model.addAttribute("user", new Users());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model)
    {
        model.addAttribute("user", new Users());
        return "index";
    }

    @GetMapping("/singUp")
    public String singUp(Model model)
    {
        model.addAttribute("user", new Users());

        return "singUp";
    }

    @GetMapping("/process_success")
    public String process_success()
    {
        return "process_success";
    }

    @PostMapping("/processRegister")
    public String processRegister(@ModelAttribute("user") Users users)
    {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encodedPassword = passwordEncoder.encode(users.getPassword());
        //users.setPassword(encodedPassword);
        userRepository.save(users);

        return "process_success";
    }

    @PostMapping("/input")
    public String input(@ModelAttribute("user") Users user, BindingResult bindingResult, Model model)
    {
            Users users = userRepository.findByEmail(user.getEmail());
            if (user.getPassword().equals(users.getPassword()))
            {
                System.out.println(users.getEmail());
                System.out.println(users.getId());
                switch (Math.toIntExact(users.getRole()))
                {
                    case 1:
                        return "admin";
                    case 2:
                        return "user";
                    case 3:
                        return "expert";
                }
            } else
            {
                model.addAttribute("message", "Неверно введены данные");
            }

        return "index";
    }
}
