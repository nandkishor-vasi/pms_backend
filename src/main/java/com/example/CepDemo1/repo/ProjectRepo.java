    package com.example.CepDemo1.repo;

    import com.example.CepDemo1.model.ProjectModel;
    import com.example.CepDemo1.model.Role;
    import com.example.CepDemo1.model.UserModel;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jdbc.core.BatchPreparedStatementSetter;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.jdbc.support.GeneratedKeyHolder;
    import org.springframework.jdbc.support.KeyHolder;
    import org.springframework.stereotype.Repository;

    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.sql.Timestamp;
    import java.util.*;

    @Repository
    public class ProjectRepo {

        @Autowired
        private JdbcTemplate jdbcTemplate;

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

            UserModel user = new UserModel();
            user.setId(rs.getLong("created_by"));
            project.setCreatedBy(user);

            return project;
        };

        public List<ProjectModel> findAll() {
            String sql = "SELECT * FROM projects";
            return jdbcTemplate.query(sql, projectRowMapper);
        }

        // Get project by ID
        public ProjectModel findById(Long id) {
            String sql = "SELECT * FROM projects WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, projectRowMapper);
        }

        public ProjectModel save(ProjectModel project) {
            String sql = "INSERT INTO projects (title, description, status, created_at, updated_at, start_date, end_date, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, project.getTitle());
                ps.setString(2, project.getDescription());
                ps.setString(3, project.getStatus().name());
                ps.setTimestamp(4, new Timestamp(project.getCreatedAt().getTime()));
                ps.setTimestamp(5, new Timestamp(project.getUpdatedAt().getTime()));
                ps.setDate(6, new java.sql.Date(project.getStartDate().getTime()));
                ps.setDate(7, new java.sql.Date(project.getEndDate().getTime()));
                ps.setLong(8, project.getCreatedBy().getId());
                return ps;
            }, keyHolder);


            Map<String, Object> keys = keyHolder.getKeys();
            Long generatedId = ((Number) Objects.requireNonNull(keys).get("id")).longValue();
            project.setId(generatedId);

            return project;
        }

        public void delete(ProjectModel project) {
            String sql = "DELETE FROM projects WHERE id = ?";
            jdbcTemplate.update(sql, project.getId());
        }

        public void addMembersToProject(Long projectId, List<Long> memberIds) {
            String sql = "INSERT INTO project_members (project_id, user_id) VALUES (?, ?)";

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, projectId);
                    ps.setLong(2, memberIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return memberIds.size();
                }
            });
        }

        public Set<UserModel> getMembersForProject(Long projectId) {
            String sql = "SELECT u.* FROM users u " +
                    "JOIN project_members pm ON u.id = pm.user_id " +
                    "WHERE pm.project_id = ?";

            return new HashSet<>(jdbcTemplate.query(sql, new Object[]{projectId}, userRowMapper));
        }

        public UserModel getAdminForProject(Long projectId) {
            String sql = "SELECT u.* FROM projects p " +
                    "JOIN users u ON p.created_by = u.id " +
                    "WHERE p.id = ?";

            // Querying for a single UserModel, since there's only one admin
            return jdbcTemplate.queryForObject(sql, new Object[]{projectId}, userRowMapper);
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


    }
