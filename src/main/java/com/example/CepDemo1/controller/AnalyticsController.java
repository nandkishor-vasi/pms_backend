package com.example.CepDemo1.controller;

import com.example.CepDemo1.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/activities/count-per-project")
    public List<Map<String, Object>> countActivitiesPerProject() {
        return analyticsService.countActivitiesPerProject();
    }

//    @GetMapping("/projects/activities-more-than-five")
//    public List<Map<String, Object>> getProjectsHavingMoreThanFiveActivities() {
//        return analyticsService.getProjectsHavingMoreThanFiveActivities();
//    }

    @GetMapping("/activities/count-by-handler")
    public List<Map<String, Object>> getActivityCountByHandler() {
        return analyticsService.getActivityCountByHandler();
    }

    @GetMapping("/activities/latest")
    public List<Map<String, Object>> getLatestActivities(@RequestParam int limit) {
        return analyticsService.getLatestActivities(limit);
    }

    @GetMapping("/projects/{projectId}/timeline")
    public List<Map<String, Object>> getActivityTimelineForProject(@PathVariable Long projectId) {
        return analyticsService.getActivityTimelineForProject(projectId);
    }
}
