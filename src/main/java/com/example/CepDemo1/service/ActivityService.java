package com.example.CepDemo1.service;

import com.example.CepDemo1.model.ActivityModel;
import com.example.CepDemo1.model.ProjectModel;
import com.example.CepDemo1.model.UserModel;
import com.example.CepDemo1.repo.ActivityRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    public List<ActivityModel> getAllActivities() {
        return activityRepo.findAll();
    }

    public ActivityModel getActivityById(Long id) {
        ActivityModel activity = activityRepo.findById(id);
        UserModel admin = activityRepo.getAdminForActivity(id);
        UserModel member = activityRepo.getMemberForActivity(id);
        ProjectModel project = activityRepo.getProjectDetailsForActivity(id);

        activity.setCreatedBy(admin);
        activity.setHandledBy(member);
        activity.setProject(project);
        return activity;
    }

    public ActivityModel createActivity(ActivityModel activity) {
        Long createdById = activity.getCreatedBy().getId();
        Long handledById = activity.getHandledBy().getId();
        Long projectId = activity.getProject().getId();

        // Save the activity to the repository
        return activityRepo.save(activity);
    }


    public ActivityModel updateActivity(Long id, ActivityModel updatedActivity) {
        ActivityModel existing = getActivityById(id);

        existing.setDetail(updatedActivity.getDetail());
        existing.setTimestamp(updatedActivity.getTimestamp());
        existing.setProject(updatedActivity.getProject());
        existing.setHandledBy(updatedActivity.getHandledBy());

        return activityRepo.save(existing);
    }

    public void deleteActivity(Long id) {
        if (!activityRepo.existsById(id)) {
            throw new RuntimeException("Activity not found with ID: " + id);
        }
        activityRepo.deleteById(id);
    }

    public List<ActivityModel> getActivitiesByAdminId(Long adminId) {

        return activityRepo.findByAdminId(adminId);
    }

}
