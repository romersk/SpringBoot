package com.bsuir.web.controllers;

import com.bsuir.web.model.Users;
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
import java.util.List;


@Controller
public class FileController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private FileExporter fileExporter;

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

}
