package com.example.CepDemo1.service;

import com.example.CepDemo1.repo.AnalyticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepo analyticsRepo;

    public List<Map<String, Object>> countActivitiesPerProject() {
        return analyticsRepo.countActivitiesPerProject();
    }

    public List<Map<String, Object>> getProjectsHavingMoreThanFiveActivities() {
        return analyticsRepo.getProjectsHavingMoreThanFiveActivities();
    }

    public List<Map<String, Object>> getActivityCountByHandler() {
        return analyticsRepo.getActivityCountByHandler();
    }

    public List<Map<String, Object>> getLatestActivities(int limit) {
        return analyticsRepo.getLatestActivities(limit);
    }

    public List<Map<String, Object>> getActivityTimelineForProject(Long projectId) {
        return analyticsRepo.getActivityTimelineForProject(projectId);
    }
}
