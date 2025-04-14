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

    @GetMapping("/activities/{projectId}/count-per-project")
    public List<Map<String, Object>> countActivitiesPerProject(@PathVariable Long projectId) {
        return analyticsService.countActivitiesPerProject(projectId);
    }

    @GetMapping("/projects/{projectId}/timeline")
    public List<Map<String, Object>> getActivityTimelineForProject(@PathVariable Long projectId) {
        return analyticsService.getActivityTimelineForProject(projectId);
    }
}
