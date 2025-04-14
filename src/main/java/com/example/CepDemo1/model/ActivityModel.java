package com.example.CepDemo1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activities")
public class ActivityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setAction(Status action) {
    }


    public enum Action{
        NOT_STARTED,IN_PROGRESS, COMPLETED
    }
    private Action action;
    private String detail;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserModel createdBy;

    @ManyToOne
    @JoinColumn(name = "handled_by")
    private UserModel handledBy;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectModel project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public UserModel getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserModel createdBy) {
        this.createdBy = createdBy;
    }

    public UserModel getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(UserModel handledBy) {
        this.handledBy = handledBy;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }
}
