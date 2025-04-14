package com.example.CepDemo1.controller;

import com.example.CepDemo1.model.ActivityModel;
import com.example.CepDemo1.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "http://localhost:3000")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/{id}")
    public ActivityModel getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

    @GetMapping("/admin/{adminId}")
    public List<ActivityModel> getActivitiesByAdminId(@PathVariable Long adminId) {
        return activityService.getActivitiesByAdminId(adminId);
    }

    @PostMapping
    public ActivityModel createActivity(@RequestBody ActivityModel activity) {
        return activityService.createActivity(activity);
    }

    @PutMapping("/{id}")
    public ActivityModel updateActivity(@PathVariable Long id, @RequestBody ActivityModel activity) {
        return activityService.updateActivity(id, activity);
    }

    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }


}
