package com.bsuir.web.controllers;

import com.bsuir.web.model.*;
import com.bsuir.web.repository.GoalsPersonRepository;
import com.bsuir.web.repository.GoalsRepository;
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
public class GoalsController {

    @Autowired
    private GoalsRepository goalsRepository;

    @Autowired
    private GoalsPersonRepository goalsPersonRepository;

    @GetMapping("/goalsList")
    public String goalsList(Model model) {
        List<Goals> goalsList = goalsRepository.findAll();
        model.addAttribute("goalsList", goalsList);
        return  "goalsList";
    }

    @PostMapping("/saveGoals")
    private String saveShoes(@ModelAttribute("goals") Goals goals)
    {
        goalsRepository.save(goals);
        return "redirect:/goalsList";
    }

    @GetMapping("/editGoals/{id}")
    public ModelAndView showGoalsPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("editGoals");
        Goals goals = goalsRepository.getOne(id);
        mav.addObject("goals", goals);

        return mav;
    }

    @GetMapping("/deleteGoals/{id}")
    public String deleteGoals(@PathVariable(name = "id") Long id) {
        goalsRepository.deleteById(id);
        return "redirect:/goalsList";
    }

    @GetMapping("/solveTask")
    public String solveTask(Model model) {

        Map<String, Double> goalsPersonList = getHashMap();


        model.addAttribute("goalsList", goalsPersonList);

        model.addAttribute("chartData", getChartData());

        return "solveTask";
    }

    @GetMapping("/singGoal")
    public String singGoal(Model model)
    {
        model.addAttribute("goals", new Goals());

        return "singGoal";
    }

    @PostMapping("/processGoal")
    public String processGoal(@ModelAttribute("goals") Goals goals, Model model)
    {
        Goals goals1 = goalsRepository.findByName(goals.getNameGoal());
        if (goals1 == null)
        {
            goalsRepository.save(goals);
        } else
        {
            model.addAttribute("message", "Цель с таким названием существует");
            return "singGoal";
        }

        List<Goals> goalsList = goalsRepository.findAll();
        model.addAttribute("goalsList", goalsList);
        return "goalsList";
    }

    private Map<String, Double> getHashMap()
    {
        List<GoalsPerson> goalsPersonList = goalsPersonRepository.findAll();
        List<GoalsPerson> list = new ArrayList<>();

        HashMap<String, Double> hashMap= new HashMap<String, Double>();

        for (int i=0; i < goalsPersonList.size(); i++) {
            String name = goalsPersonList.get(i).getGoals().getNameGoal();

            Double cost = goalsPersonList.get(i).getRate();
            cost = (double) Math.round(cost * 1000) / 1000;

            if (hashMap.containsKey(name)) {
                double sum = hashMap.get(name);
                sum += cost;
                sum = (double) Math.round(sum * 1000) / 1000;
                hashMap.put(name, sum);
            } else {
                hashMap.put(name, cost);
            }

        }

        Map<String, Double> map = sortByValues(hashMap);

        return map;
    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    private List<List<Object>> getChartData() {

        List<GoalsPerson> goalsPersonList = goalsPersonRepository.findAll();

        List<List<Object>> list = new ArrayList<>();

        HashMap<String, Double> hashMap= new HashMap<String, Double>();

        for (int i=0; i < goalsPersonList.size(); i++) {
            String name = goalsPersonList.get(i).getGoals().getNameGoal();

            Double cost = goalsPersonList.get(i).getRate();

            if (hashMap.containsKey(name)) {
                double sum = hashMap.get(name);
                sum += cost;
                hashMap.put(name, sum);
            } else {
                hashMap.put(name, cost);
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
