package com.bsuir.web.controllers;

import com.bsuir.web.model.*;
import com.bsuir.web.repository.GoalsRepository;
import com.bsuir.web.repository.PersonRepository;
import com.bsuir.web.repository.ShoesRepository;
import com.bsuir.web.repository.UserRepository;
//import com.bsuir.web.service.CustomUserDetails;
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
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ShoesRepository shoesRepository;

    @Autowired
    private GoalsRepository goalsRepository;

    private Users customUserDetails;

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
                    customUserDetails = new Users();
                    customUserDetails.setId(users.getId());
                    customUserDetails.setRole(users.getRole());
                    customUserDetails.setEmail(users.getEmail());
                    customUserDetails.setPassword(users.getPassword());
                    customUserDetails.setPerson(users.getPerson());
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
            if (usersList.get(i).getEmail().equals(customUserDetails.getEmail()))
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
        users.setEmail(customUserDetails.getEmail());
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

    @GetMapping("/logout")
    public String homePage(Model model)
    {
        model.addAttribute("user", new Users());
        return "index";
    }

    @PostMapping("/saveAdmin")
    private String saveAdmin(@ModelAttribute("user") Users users)
    {
        userRepository.save(users);
        customUserDetails = new Users();
        customUserDetails.setId(users.getId());
        customUserDetails.setRole(users.getRole());
        customUserDetails.setEmail(users.getEmail());
        customUserDetails.setPassword(users.getPassword());
        customUserDetails.setPerson(users.getPerson());
        //customUserDetails = new CustomUserDetails(users);
        return "redirect:/adminInfo";
    }

    @GetMapping("/userInfo")
    private String userInfo(Model model)
    {
        Users users = new Users();
        users.setId(customUserDetails.getId());
        users.setEmail(customUserDetails.getEmail());
        users.setPassword(customUserDetails.getPassword());
        users.setRole(customUserDetails.getRole());
        users.setPerson(customUserDetails.getPerson());
        model.addAttribute("user", users);
        return "userInfo";
    }

    @PostMapping("/processShoes")
    public String processShoes(@ModelAttribute("shoes")Shoes shoes, Model model)
    {
        Shoes shoes1 = shoesRepository.findByName(shoes.getNameShoes());
        if (shoes1 == null)
        {
            Person person = personRepository.findById(customUserDetails.getPerson().getIdPerson()).get();
            shoes.setPerson(person);
            shoesRepository.save(shoes);
        } else
        {
            model.addAttribute("message", "Обувь с таким названием существует");
            return "createShoes";
        }

        return "user";
    }

    @GetMapping("/userShoesList")
    public String userShoesList(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();

        List<Shoes> shoesCollection = new ArrayList<>();

        for (int i = 0; i < shoesList.size(); i++) {
            if (shoesList.get(i).getPerson().getIdPerson().equals(customUserDetails.getPerson().getIdPerson())) {
                shoesCollection.add(shoesList.get(i));
            }
        }

        model.addAttribute("shoesList", shoesCollection);
        return "userShoesList";
    }

    @PostMapping("/saveShoesUser")
    private String saveShoes(@ModelAttribute("shoes") Shoes shoes)
    {
        Person person = personRepository.findById(customUserDetails.getPerson().getIdPerson()).get();
        shoes.setPerson(person);
        shoesRepository.save(shoes);
        return "redirect:/userShoesList";
    }

    @GetMapping("/createShoes")
    public String createShoes(Model model)
    {
        model.addAttribute("shoes", new Shoes());
        return "createShoes";
    }

    @GetMapping("/editShoesUser/{id}")
    public ModelAndView showShoesUser(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("editShoesUser");
        Shoes shoes = shoesRepository.getOne(id);
        mav.addObject("shoes", shoes);

        return mav;
    }

    @PostMapping("/deleteShoesUser/{id}")
    public String deleteShoesUser(@PathVariable(name = "id") Long id) {
        shoesRepository.deleteById(id);
        return "redirect:/userShoesList";
    }

    @GetMapping("/expertInfo")
    private String expertInfo(Model model)
    {
        Users users = new Users();
        users.setId(customUserDetails.getId());
        users.setEmail(customUserDetails.getEmail());
        users.setPassword(customUserDetails.getPassword());
        users.setRole(customUserDetails.getRole());
        users.setPerson(customUserDetails.getPerson());
        model.addAttribute("user", users);
        return "expertInfo";
    }

    @GetMapping("/expertRate")
    private String expertRate(Model model) {
        List<Goals> goalsList = goalsRepository.findAll();
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i< goalsList.size(); i++) {
            Integer inte = (i+1);
            String state = 'Z' + inte.toString();
            map.put(state, goalsList.get(i).getNameGoal());

        }

        int size = goalsList.size();
        int sizeX = size*(size-1);
        model.addAttribute("goalList", map);
        model.addAttribute("size", Long.valueOf(sizeX));

        Long[][] matrix = new Long[size][size];
        model.addAttribute("matrix", matrix);

        model.addAttribute("formBean", new FormBean(getVerteilungenMatrix(size)));

        return "expertRate";
    }

    private List<List<Long>> getVerteilungenMatrix(int size) {
        List<List<Long>> result2 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result2.add(new ArrayList<>());

            for (int j=0; j <size; j++) {
                result2.get(i).add(Long.valueOf(0));
            }
        }
        return result2;
    }

    @PostMapping("/processTask")
    public String processTask(@ModelAttribute("formBean") FormBean formBean, Model model)
    {

        List<List<Long>> list= formBean.getMatrix();
        int sizeMax = list.size() * (list.size()-1);

        for (int i=0; i < list.size(); i++) {
            for (int j=i+1; j < list.size(); j++) {
                Long temp = Long.valueOf(sizeMax) - list.get(i).get(j);
                list.get(j).set(i, temp);
            }
        }

        List<Double> 
        

        return "expert";
    }
}
