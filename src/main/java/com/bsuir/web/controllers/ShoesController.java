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

import java.util.*;

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

    @GetMapping("/sortById")
    public String sortId(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        Collections.sort(shoesList, new Comparator<Shoes>() {
            public int compare(Shoes o1, Shoes o2) {
                return o1.getIdShoes().compareTo(o2.getIdShoes());
            }
        });
        model.addAttribute("shoesList", shoesList);
        return "shoesList";
    }

    @GetMapping("/sortByName")
    public String sortByName(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        Collections.sort(shoesList, new Comparator<Shoes>() {
            public int compare(Shoes o1, Shoes o2) {
                return o1.getNameShoes().compareTo(o2.getNameShoes());
            }
        });
        model.addAttribute("shoesList", shoesList);
        return "shoesList";
    }

    @GetMapping("/sortByCosts")
    public String sortByCosts(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        Collections.sort(shoesList, new Comparator<Shoes>() {
            public int compare(Shoes o1, Shoes o2) {
                return o1.getCosts().compareTo(o2.getCosts());
            }
        });
        model.addAttribute("shoesList", shoesList);
        return "shoesList";
    }

    @GetMapping("/filtrate")
    public String filtrate(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        model.addAttribute("shoesList", shoesList);
        return "filtrate";
    }

    @GetMapping("/filtrateCheap")
    public String filtrateCheap(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        List<Shoes> outList = new ArrayList<>();
        for (int i=0; i < shoesList.size(); i++) {
            if (shoesList.get(i).getCosts().compareTo(50.0) <= 0) {
                outList.add(shoesList.get(i));
            }
        }
        if (outList.isEmpty()) {
            model.addAttribute("message", "По данному параметру ничего не найдено");
        } else {
            model.addAttribute("shoesList", outList);
        }
        return "filtrate";
    }

    @GetMapping("/filtrateExpensive")
    public String filtrateExpensive(Model model)
    {
        List<Shoes> shoesList = shoesRepository.findAll();
        List<Shoes> outList = new ArrayList<>();
        for (int i=0; i < shoesList.size(); i++) {
            if (shoesList.get(i).getCosts().compareTo(150.0) >= 0) {
                outList.add(shoesList.get(i));
            }
        }
        if (outList.isEmpty()) {
            model.addAttribute("message", "По данному параметру ничего не найдено");
        } else {
            model.addAttribute("shoesList", outList);
        }
        return "filtrate";
    }

    @GetMapping("/filtratePeople")
    public String filtratePeople(Model model)
    {
        List<Person> personList = personRepository.findAll();

        Collections.sort(personList, new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                if (o1.getShoesCollection().size() < o2.getShoesCollection().size()) {
                    return 1;
                } else if (o1.getShoesCollection().size() > o2.getShoesCollection().size()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        model.addAttribute("shoesList", personList.get(0).getShoesCollection());

        return "filtrate";
    }
}
