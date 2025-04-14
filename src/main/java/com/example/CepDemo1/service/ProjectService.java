package com.example.CepDemo1.service;

import com.example.CepDemo1.model.ProjectModel;
import com.example.CepDemo1.model.UserModel;
import com.example.CepDemo1.repo.ProjectRepo;
import com.example.CepDemo1.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    // Get all projects
    public List<ProjectModel> getAllProjects() {
        return projectRepo.findAll();
    }

    // Get project by ID
    public ProjectModel getProjectById(Long id) {
        return projectRepo.findById(id);
    }

    // Create a new project
    public ProjectModel createProject(ProjectModel project) {
        project.setCreatedAt(new Date());
        project.setUpdatedAt(new Date());
        return projectRepo.save(project);
    }


    public ProjectModel updateProject(Long id, ProjectModel updatedProject) {
        ProjectModel existingProject = getProjectById(id);

        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setStatus(updatedProject.getStatus());
        existingProject.setStartDate(updatedProject.getStartDate());
        existingProject.setEndDate(updatedProject.getEndDate());
        existingProject.setUpdatedAt(new Date());

        return projectRepo.save(existingProject);
    }

    // Delete a project
    public void deleteProject(Long id) {
        ProjectModel project = getProjectById(id);
        projectRepo.delete(project);
    }

    public void addMembersToProject(Long projectId, List<Long> memberIds) {
        projectRepo.addMembersToProject(projectId, memberIds);

        Set<UserModel> members = projectRepo.getMembersForProject(projectId);
        ProjectModel project = projectRepo.findById(projectId);
        project.setMembers(members);
    }

    public ProjectModel getProjectDetails(Long projectId) {
        ProjectModel project = projectRepo.findById(projectId);
        Set<UserModel> members = projectRepo.getMembersForProject(projectId);
        UserModel user = projectRepo.getAdminForProject(projectId);
        project.setCreatedBy(user);
        project.setMembers(members);
        return project;
    }

}
