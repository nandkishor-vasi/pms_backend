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


    public List<Map<String, Object>> countActivitiesPerProject(Long projectId) {
        String sql = "SELECT project_id, COUNT(*) AS total_activities " +
                "FROM activities " +
                "WHERE (? IS NULL OR project_id = ?) " +
                "GROUP BY project_id";
        return jdbcTemplate.queryForList(sql, projectId, projectId);
    }

    public List<Map<String, Object>> getActivityTimelineForProject(Long projectId) {
        String sql = "SELECT a.id, a.action, a.timestamp, u.username AS handled_by " +
                "FROM activities a JOIN users u ON a.handled_by = u.id " +
                "WHERE a.project_id = ? ORDER BY a.timestamp ASC";
        return jdbcTemplate.queryForList(sql, projectId);
    }
}
