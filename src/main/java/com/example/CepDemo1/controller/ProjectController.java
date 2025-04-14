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
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<ProjectModel> getAllProjects() {
        return projectService.getAllProjects();
    }


    @PostMapping
    public ProjectModel createProject(@RequestBody ProjectModel project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    public ProjectModel updateProject(@PathVariable Long id, @RequestBody ProjectModel project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PostMapping("/{projectId}/add-members")
    public ResponseEntity<String> addMembersToProject(@PathVariable Long projectId, @RequestBody List<Long> memberIds) {
        try {
            projectService.addMembersToProject(projectId, memberIds);
            return ResponseEntity.ok("Members added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding members: " + e.getMessage());
        }
    }

    @GetMapping("/{projectId}")
    public ProjectModel getProjectDetails(@PathVariable Long projectId) {
        return projectService.getProjectDetails(projectId);
    }

}
