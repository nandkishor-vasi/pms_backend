package com.example.CepDemo1.service;

import com.example.CepDemo1.model.ProjectModel;
import com.example.CepDemo1.model.UserModel;
import com.example.CepDemo1.repo.AnalyticsRepo;
import com.example.CepDemo1.repo.ProjectRepo;
import com.example.CepDemo1.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AnalyticsRepo analyticsRepo;

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

    public List<UserModel> getAssignedMembersForProject(Long projectId) {
        // Get all users assigned to the project
        Set<UserModel> assignedMembers = projectRepo.getMembersForProject(projectId);

        // Convert the Set to a List
        List<UserModel> assignedMembersList = new ArrayList<>(assignedMembers);

        // Return the list of assigned members
        return assignedMembersList;
    }

    public Map<String, Object> getProjectTimeline(Long projectId) {
        return analyticsRepo.getProjectTimeline(projectId);
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

    // Get projects for a member by memberId
    public List<ProjectModel> getProjectByMemberId(Long memberId) {
        List<ProjectModel> projects = projectRepo.findByMemberId(memberId);
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
