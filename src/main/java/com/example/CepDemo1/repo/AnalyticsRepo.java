package com.example.CepDemo1.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AnalyticsRepo  {

    @Autowired
    private JdbcTemplate jdbcTemplate;

     
    public List<Map<String, Object>> countActivitiesPerProject() {
        String sql = "SELECT project_id, COUNT(*) AS total_activities " +
                "FROM activities GROUP BY project_id";
        return jdbcTemplate.queryForList(sql);
    }

     
    public List<Map<String, Object>> getProjectsHavingMoreThanFiveActivities() {
        String sql = "SELECT project_id, COUNT(*) AS total_activities " +
                "FROM activities GROUP BY project_id HAVING COUNT(*) > 5";
        return jdbcTemplate.queryForList(sql);
    }

     
    public List<Map<String, Object>> getActivityCountByHandler() {
        String sql = "SELECT handled_by, COUNT(*) AS handled_count " +
                "FROM activities GROUP BY handled_by";
        return jdbcTemplate.queryForList(sql);
    }

     
    public List<Map<String, Object>> getLatestActivities(int limit) {
        String sql = "SELECT * FROM activities ORDER BY timestamp DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, limit);
    }

     
    public List<Map<String, Object>> getActivityTimelineForProject(Long projectId) {
        String sql = "SELECT a.id, a.action, a.timestamp, u.username AS handled_by " +
                "FROM activities a JOIN users u ON a.handled_by = u.id " +
                "WHERE a.project_id = ? ORDER BY a.timestamp ASC";
        return jdbcTemplate.queryForList(sql, projectId);
    }
}
