package com.bsuir.web.controllers;

import com.bsuir.web.model.Person;
import com.bsuir.web.model.Users;
import com.bsuir.web.repository.PersonRepository;
import com.bsuir.web.repository.UserRepository;
import com.bsuir.web.service.CustomUserDetails;
import com.bsuir.web.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UsersService usersService;


    private CustomUserDetails customUserDetails;

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
        model.addAttribute("person", new Person());

        return "singUp";
    }

    @GetMapping("/process_success")
    public String process_success()
    {
        return "process_success";
    }

    @PostMapping("/processRegister")
    public String processRegister(@ModelAttribute("user") Users users, @ModelAttribute("person") Person person, Model model)
    {
        Users user = userRepository.findByEmail(users.getEmail());
        if (user == null)
        {
            personRepository.save(person);
            System.out.println(person.getIdPerson());
            users.setPerson(person);
            userRepository.save(users);
        } else
        {
            model.addAttribute("message", "Пользователь с таким адресом почты существует");
            return "singUp";
        }

        return "process_success";
    }

    @PostMapping("/input")
    public String input(@ModelAttribute("user") Users user, BindingResult bindingResult, Model model)
    {
            Users users = userRepository.findByEmail(user.getEmail());
            if (users != null)
            {
                if (user.getPassword().equals(users.getPassword()))
                {
                    customUserDetails = new CustomUserDetails(users);
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
            } else
            {
                model.addAttribute("message", "Неверно введены данные");
            }


        return "index";
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

    @GetMapping("/admin")
    public String adminPage()
    {
        return "admin";
    }

    @GetMapping("/adminUserList")
    public String userList(Model model)
    {
        List<Users> usersList = usersService.listAll();
        int deleteNum = -1;
        for (int i = 0; i < usersList.size(); i++)
        {
            if (usersList.get(i).getEmail().equals(customUserDetails.getUsername()))
            {
                deleteNum = i;
            }
        }
        usersList.remove(deleteNum);
        model.addAttribute("userList", usersList);
        return "adminUserList";
    }

    @GetMapping("/adminInfo")
    private String adminInfo(Model model)
    {
        Users users = new Users();
        users.setId(customUserDetails.getId());
        users.setEmail(customUserDetails.getUsername());
        users.setPassword(customUserDetails.getPassword());
        users.setRole(customUserDetails.getRole());
        users.setPerson(customUserDetails.getPerson());
        model.addAttribute("user", users);
        return "adminInfo";
    }

    @PostMapping("/save")
    private String saveUser(@ModelAttribute("user") Users users)
    {
        userRepository.save(users);
        return "redirect:/adminUserList";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("editUsers");
        Users users = usersService.get(id);
        mav.addObject("user", users);

        return mav;
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        usersService.delete(id);
        return "redirect:/adminUserList";
    }

    @GetMapping("/editAdmin/{id}")
    public ModelAndView showEditPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("editAdmin");
        Users users = usersService.get(id);
        mav.addObject("user", users);

        return mav;
    }

    @PostMapping("/saveAdmin")
    private String saveAdmin(@ModelAttribute("user") Users users)
    {
        userRepository.save(users);
        customUserDetails = new CustomUserDetails(users);
        return "redirect:/adminInfo";
    }
}
