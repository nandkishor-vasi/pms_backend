package com.example.CepDemo1.service;

import com.example.CepDemo1.model.ActivityModel;
import com.example.CepDemo1.repo.ActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    // Get all activity logs
    public List<ActivityModel> getAllActivities() {
        return activityRepo.findAll();
    }

    // Get a single activity by ID
    public ActivityModel getActivityById(Long id) {
        return activityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with ID: " + id));
    }

    // Create a new activity
    public ActivityModel createActivity(ActivityModel activity) {

        return activityRepo.save(activity);
    }

    // Update an activity by ID
    public ActivityModel updateActivity(Long id, ActivityModel updatedActivity) {
        ActivityModel existing = getActivityById(id);

        existing.setDetail(updatedActivity.getDetail());
        existing.setTimestamp(updatedActivity.getTimestamp());
        existing.setProject(updatedActivity.getProject());
        existing.setHandledBy(updatedActivity.getHandledBy());

        return activityRepo.save(existing);
    }

    // Delete activity by ID
    public void deleteActivity(Long id) {
        if (!activityRepo.existsById(id)) {
            throw new RuntimeException("Activity not found with ID: " + id);
        }
        activityRepo.deleteById(id);
    }
}
