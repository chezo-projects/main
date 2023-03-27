package com.zsoltnagy.app.thirdparty.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zsoltnagy.app.thirdparty.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"chezo.hu", "zsolt-nagy.com", "https://chezo.hu", "https://zsolt-nagy.com", "http://localhost:3000"})
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/latest")
    public JsonNode latest() {
        List<String> repoNames = projectService.getRepoNames(1);

        return projectService.getRepoData(repoNames.get(0));
    }


    @GetMapping("/latestSmall")
    public JsonNode latestSmall() {
        List<String> repoNames = projectService.getRepoNames(2);
        return projectService.getRepoData(repoNames.get(1));
    }

    @GetMapping("/other")
    public List<JsonNode> otherProjects() {
        List<String> repoNames = projectService.getRepoNames(5);
        List<JsonNode> result = new ArrayList<>();

        for (int i = 2; i < repoNames.size(); i++) {
            result.add(projectService.getRepoData(repoNames.get(i)));
        }
        return result;
    }


}
