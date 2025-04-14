package com.example.CepDemo1.controller;

import com.example.CepDemo1.model.ProjectModel;

import java.util.List;

public class ProjectWithMembers {
    private ProjectModel project;
    private List<Long> memberIds;

    // Getters and setters
    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }
}
