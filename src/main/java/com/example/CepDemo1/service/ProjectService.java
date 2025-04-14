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
        List<ProjectModel> projects = projectRepo.findAll();
        for (ProjectModel project : projects) {
            Long projectId = project.getId();

            // Set the creator (admin)
            UserModel admin = projectRepo.getAdminForProject(projectId);
            project.setCreatedBy(admin);

            // Set the members
            Set<UserModel> members = projectRepo.getMembersForProject(projectId);
            project.setMembers(members);
        }
        return projects;
    }

    // Get project by ID
    public ProjectModel getProjectById(Long id) {
        return projectRepo.findById(id);
    }

    // Create a new project
    public ProjectModel createProject(ProjectModel project, List<Long> memberIds) {
        project.setCreatedAt(new Date());
        project.setUpdatedAt(new Date());
        return projectRepo.save(project, memberIds);
    }


    public ProjectModel updateProject(Long id, ProjectModel updatedProject, List<Long> memberIds) {
        ProjectModel existingProject = getProjectById(id);

        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setStatus(updatedProject.getStatus());
        existingProject.setStartDate(updatedProject.getStartDate());
        existingProject.setEndDate(updatedProject.getEndDate());
        existingProject.setUpdatedAt(new Date());

        return projectRepo.save(existingProject, memberIds);
    }

    // Delete a project
    public void deleteProject(Long id) {
        ProjectModel project = getProjectById(id);
        projectRepo.delete(project);
    }

    public List<ProjectModel> getProjectsByUserId(Long userId) {
        List<ProjectModel> projects = projectRepo.findByUserId(userId);
        for (ProjectModel project : projects) {
            Long projectId = project.getId();

            UserModel admin = projectRepo.getAdminForProject(projectId);
            project.setCreatedBy(admin);

            Set<UserModel> members = projectRepo.getMembersForProject(projectId);
            project.setMembers(members);
        }
        return projects;
    }

}
