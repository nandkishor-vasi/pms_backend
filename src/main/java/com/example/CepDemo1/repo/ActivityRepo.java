package com.example.CepDemo1.repo;

import com.example.CepDemo1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ActivityRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ActivityModel> activityRowMapper = (rs, rowNum) -> {
        ActivityModel activity = new ActivityModel();

        activity.setId(rs.getLong("id"));
        activity.setAction(Status.valueOf(rs.getString("action")));
        activity.setDetail(rs.getString("detail"));
        activity.setTimestamp(rs.getTimestamp("timestamp"));

        // CreatedBy (UserModel with only ID)
        UserModel createdBy = new UserModel();
        createdBy.setId(rs.getLong("created_by"));
        activity.setCreatedBy(createdBy);

        // HandledBy (UserModel with only ID)
        UserModel handledBy = new UserModel();
        handledBy.setId(rs.getLong("handled_by"));
        activity.setHandledBy(handledBy);

        // Project (ProjectModel with only ID)
        ProjectModel project = new ProjectModel();
        project.setId(rs.getLong("project_id"));
        activity.setProject(project);

        return activity;
    };

    public List<ActivityModel> findAll() {
        String sql = "SELECT * FROM activities";
        return jdbcTemplate.query(sql, activityRowMapper);
    }

    public Optional<ActivityModel> findById(Long id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        List<ActivityModel> result = jdbcTemplate.query(sql, activityRowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public ActivityModel save(ActivityModel activity) {
        if (activity.getId() == null) {
            // INSERT new activity
            String insertSql = "INSERT INTO activities (action, detail, timestamp, created_by, handled_by, project_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(insertSql,
                    activity.getAction(),
                    activity.getDetail(),
                    activity.getTimestamp(),
                    activity.getCreatedBy().getId(),
                    activity.getHandledBy().getId(),
                    activity.getProject().getId()
            );

            // Get auto-generated ID
            Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            activity.setId(id);

            // Update project status to IN_PROGRESS by default after creating an activity
            updateProjectStatus(activity.getProject().getId(), "IN_PROGRESS");

        } else {
            // UPDATE existing activity
            String updateSql = "UPDATE activities SET action = ?, detail = ?, timestamp = ?, created_by = ?, " +
                    "handled_by = ?, project_id = ? WHERE id = ?";

            jdbcTemplate.update(updateSql,
                    activity.getAction(),
                    activity.getDetail(),
                    activity.getTimestamp(),
                    activity.getCreatedBy().getId(),
                    activity.getHandledBy().getId(),
                    activity.getProject().getId(),
                    activity.getId()
            );

            if ("COMPLETED".equalsIgnoreCase(activity.getAction().toString())) {
                updateProjectStatus(activity.getProject().getId(), "COMPLETED");
            } else {
                updateProjectStatus(activity.getProject().getId(), "IN_PROGRESS");
            }
        }

        return activity;
    }

    private void updateProjectStatus(Long projectId, String status) {
        String sql = "UPDATE projects SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, projectId);
    }


    public void deleteById(Long id) {
        String sql = "DELETE FROM activities WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM activities WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

}
