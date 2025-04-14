package com.example.CepDemo1.controller;

import com.example.CepDemo1.model.ProjectModel;
import com.example.CepDemo1.model.UserModel;
import com.example.CepDemo1.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/projectByAdmin/{userId}")
    public List<ProjectModel> getProjectsWithUserId(@PathVariable Long userId){
        return projectService.getProjectsByUserId(userId);
    }

    @GetMapping("/availableMembers/{projectId}")
    public List<UserModel> getAvailableMembersForProject(@PathVariable Long projectId) {
        return projectService.getAssignedMembersForProject(projectId);
    }

    @PostMapping
    public ProjectModel createProject(@RequestBody ProjectModel project) {
        project.setCreatedAt(new Date());
        project.setUpdatedAt(new Date());

        List<Long> memberIds = project.getMembers().stream()
                .map(UserModel::getId)
                .collect(Collectors.toList());


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
