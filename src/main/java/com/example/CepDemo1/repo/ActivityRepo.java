package com.example.CepDemo1.repo;

import com.example.CepDemo1.model.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ActivityRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Enumerated(EnumType.STRING)
    private Action action;

    private final RowMapper<ActivityModel> activityRowMapper = (rs, rowNum) -> {
        ActivityModel activity = new ActivityModel();

        activity.setId(rs.getLong("id"));
        activity.setAction(Action.valueOf(rs.getString("action")));
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

    public ActivityModel findById(Long id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, activityRowMapper, id);
    }


    public ActivityModel save(ActivityModel activity) {
        if (activity.getId() == null) {
            // INSERT new activity using KeyHolder
            String insertSql = "INSERT INTO activities (action, detail, timestamp, created_by, handled_by, project_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, activity.getAction().toString());
                ps.setString(2, activity.getDetail());
                ps.setTimestamp(3, new java.sql.Timestamp(activity.getTimestamp().getTime()));
                ps.setLong(4, activity.getCreatedBy().getId());
                ps.setLong(5, activity.getHandledBy().getId());
                ps.setLong(6, activity.getProject().getId());
                return ps;
            }, keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();
            Long generatedId = ((Number) Objects.requireNonNull(keys).get("id")).longValue();
            activity.setId(generatedId);

            updateProjectStatus(activity.getProject().getId(), "IN_PROGRESS");

        } else {
            // UPDATE logic remains the same
            String updateSql = "UPDATE activities SET action = ?, detail = ?, timestamp = ?, created_by = ?, " +
                    "handled_by = ?, project_id = ? WHERE id = ?";

            jdbcTemplate.update(updateSql,
                    activity.getAction().toString(),
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

    public UserModel getAdminForActivity(Long activityId) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN activities a ON u.id = a.created_by WHERE a.id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, activityId);
    }

    public UserModel getMemberForActivity(Long activityId) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN activities a ON u.id = a.handled_by WHERE a.id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, activityId);
    }

    public ProjectModel getProjectDetailsForActivity(Long activityId) {
        String sql = "SELECT p.* FROM projects p " +
                "JOIN activities a ON p.id = a.project_id WHERE a.id = ?";
        return jdbcTemplate.queryForObject(sql, projectRowMapper, activityId);
    }

    private final RowMapper<UserModel> userRowMapper = (rs, rowNum) -> {
        UserModel user = new UserModel();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
    };

    private final RowMapper<ProjectModel> projectRowMapper = (rs, rowNum) -> {
        ProjectModel project = new ProjectModel();
        project.setId(rs.getLong("id"));
        project.setTitle(rs.getString("title"));
        project.setDescription(rs.getString("description"));
        project.setStatus(ProjectModel.Status.valueOf(rs.getString("status")));
        project.setCreatedAt(rs.getTimestamp("created_at"));
        project.setUpdatedAt(rs.getTimestamp("updated_at"));
        project.setStartDate(rs.getDate("start_date"));
        project.setEndDate(rs.getDate("end_date"));
        return project;
    };

    public List<ActivityModel> findByAdminId(Long adminId) {
        String sql = "SELECT * FROM activities WHERE created_by = ?";
        return jdbcTemplate.query(sql, activityRowMapper, adminId);
    }

    public List<ActivityModel> findActivitiesForMember(Long memberId) {
        String sql = """
        SELECT 
            a.id AS activity_id,
            a.action,
            a.detail,
            a.timestamp,
            a.created_by,
            a.handled_by,
            p.id AS project_id,
            p.title AS project_title,
            p.description AS project_description,
            p.status AS project_status,
            p.start_date,
            p.end_date,
            p.created_at,
            p.updated_at
        FROM activities a
        JOIN projects p ON a.project_id = p.id
        WHERE a.handled_by = ?
        ORDER BY a.timestamp DESC
        """;

        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            ActivityModel activity = new ActivityModel();
            activity.setId(rs.getLong("activity_id"));
            activity.setAction(Action.valueOf(rs.getString("action")));
            activity.setDetail(rs.getString("detail"));
            activity.setTimestamp(rs.getTimestamp("timestamp"));

            // Minimal createdBy reference
            UserModel createdBy = new UserModel();
            createdBy.setId(rs.getLong("created_by"));
            activity.setCreatedBy(createdBy);

            // Member (handled_by)
            UserModel handledBy = new UserModel();
            handledBy.setId(rs.getLong("handled_by"));
            activity.setHandledBy(handledBy);

            // Project details
            ProjectModel project = new ProjectModel();
            project.setId(rs.getLong("project_id"));
            project.setTitle(rs.getString("project_title"));
            project.setDescription(rs.getString("project_description"));
            project.setStatus(ProjectModel.Status.valueOf(rs.getString("project_status")));
            project.setStartDate(rs.getDate("start_date"));
            project.setEndDate(rs.getDate("end_date"));
            project.setCreatedAt(rs.getTimestamp("created_at"));
            project.setUpdatedAt(rs.getTimestamp("updated_at"));
            activity.setProject(project);

            return activity;
        });
    }

    public List<ActivityModel> findByProjectId(Long projectId) {
        String sql = "SELECT * FROM activities WHERE project_id = ?";
        return jdbcTemplate.query(sql, activityRowMapper, projectId);
    }
}
