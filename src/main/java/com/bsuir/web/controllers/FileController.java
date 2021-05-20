package com.bsuir.web.controllers;

import com.bsuir.web.model.GoalsPerson;
import com.bsuir.web.model.Users;
import com.bsuir.web.repository.GoalsPersonRepository;
import com.bsuir.web.service.FileExporter;
import com.bsuir.web.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;


@Controller
public class FileController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private FileExporter fileExporter;

    @Autowired
    private GoalsPersonRepository goalsPersonRepository;

    @GetMapping("/downloadUsers")
    public ResponseEntity<InputStreamResource> downloadTextFileExample1() throws FileNotFoundException {
        String fileName = "users.txt";
        List<Users> userList = usersService.listAll();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < userList.size(); i++) {
            stringBuilder.append(userList.get(i).getId());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getEmail());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getPerson().getFirstName());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getPerson().getSecondName());
            stringBuilder.append(" ");
            stringBuilder.append(userList.get(i).getPerson().getWorkPlace());
            stringBuilder.append("\n");
        }

        String fileContent = stringBuilder.toString();

        // Create text file
        Path exportedPath = fileExporter.export(fileContent, fileName);

        // Download file with InputStreamResource
        File exportedFile = exportedPath.toFile();
        FileInputStream fileInputStream = new FileInputStream(exportedFile);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(exportedFile.length())
                .body(inputStreamResource);
    }

    @GetMapping("/downloadRate")
    public ResponseEntity<InputStreamResource> downloadTextFileExample2() throws FileNotFoundException {
        String fileName = "rates.txt";

        Map<String, Double> goalsPersonList = getHashMap();
        StringBuilder stringBuilder = new StringBuilder();

        if (!goalsPersonList.isEmpty()) {

            stringBuilder.append(goalsPersonList);
            stringBuilder.append("\n");

        }

        String fileContent = stringBuilder.toString();

        // Create text file
        Path exportedPath = fileExporter.export(fileContent, fileName);

        // Download file with InputStreamResource
        File exportedFile = exportedPath.toFile();
        FileInputStream fileInputStream = new FileInputStream(exportedFile);
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(exportedFile.length())
                .body(inputStreamResource);
    }

    private Map<String, Double> getHashMap()
    {
        List<GoalsPerson> goalsPersonList = goalsPersonRepository.findAll();
        List<GoalsPerson> list = new ArrayList<>();

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

}
