package com.example.CepDemo1.controller;

import com.example.CepDemo1.model.ProjectModel;
import com.example.CepDemo1.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<ProjectModel> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/projectByAdmin/{userId}")
    public List<ProjectModel> getProjectsWithUserId(@PathVariable Long userId){
        return projectService.getProjectsByUserId(userId);
    }

    @PostMapping
    public ProjectModel createProject(@RequestBody ProjectWithMembers projectWithMembers) {
        ProjectModel project = projectWithMembers.getProject();
        List<Long> memberIds = projectWithMembers.getMemberIds();

        System.out.println("Incoming project: " + projectWithMembers.getProject());
        System.out.println("CreatedBy: " + projectWithMembers.getProject().getCreatedBy());
        System.out.println("Member IDs: " + projectWithMembers.getMemberIds());


        return projectService.createProject(project, memberIds);
    }

    @PutMapping("/{id}")
    public ProjectModel updateProject(@PathVariable Long id, @RequestBody ProjectModel project,  @RequestBody List<Long> memberIds) {
        return projectService.updateProject(id, project, memberIds);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }



}
