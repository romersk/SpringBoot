package com.bsuir.web.controllers;

import com.bsuir.web.model.Person;
import com.bsuir.web.model.Shoes;
import com.bsuir.web.repository.PersonRepository;
import com.bsuir.web.repository.ShoesRepository;
import com.bsuir.web.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShoesController {

    @Autowired
    private ShoesRepository shoesRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PersonRepository personRepository;


    @GetMapping("/shoesList")
    public String shoesList(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        System.out.println(shoesList.get(0).getIdShoes());
        model.addAttribute("shoesList", shoesList);
        return "shoesList";

    }

    @PostMapping("/saveShoes")
    private String saveShoes(@ModelAttribute("shoes") Shoes shoes)
    {
        shoesRepository.save(shoes);
        return "redirect:/shoesList";
    }

    @GetMapping("/editShoes/{id}")
    public ModelAndView showShoesPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("editShoes");
        Shoes shoes = shoesRepository.getOne(id);
        mav.addObject("shoes", shoes);

        return mav;
    }

    @PostMapping("/deleteShoes/{id}")
    public String deleteShoes(@PathVariable(name = "id") Long id) {
        shoesRepository.deleteById(id);
        return "redirect:/shoesList";
    }

    @GetMapping("/graph")
    public String graph(Model model)
    {
        model.addAttribute("chartData", getChartData());
        return "graph";
    }

    private List<List<Object>> getChartData() {

        List<Shoes> shoesList = shoesRepository.findAll();

        List<List<Object>> list = new ArrayList<>();

        HashMap<String, Double> hashMap= new HashMap<String, Double>();

        for (int i=0; i < shoesList.size(); i++) {
            String fio = shoesList.get(i).getPerson().getFirstName() + ' ' + shoesList.get(i).getPerson().getSecondName();

            Double cost = shoesList.get(i).getCosts();

            if (hashMap.containsKey(fio)) {
                double sum = hashMap.get(fio);
                sum += cost;
                hashMap.put(fio, sum);
            } else {
                hashMap.put(fio, cost);
            }

        }

        for(Map.Entry<String, Double> item : hashMap.entrySet()){

            List<Object> objectList = List.of(item.getKey(), item.getValue());
            list.add(objectList);
        }

        /*return List.of(
                List.of("Mushrooms", 5),
                List.of("Onions", 4),
                List.of("Olives", 3),
                List.of("Zucchini", 10),
                List.of("Pepperoni", 4)
        );*/
        return list;
    }
}
