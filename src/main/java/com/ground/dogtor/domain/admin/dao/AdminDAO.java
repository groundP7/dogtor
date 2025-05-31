package com.ground.dogtor.domain.admin.dao;

import com.ground.dogtor.domain.admin.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class AdminDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Admin> adminRowMapper = new AdminRowMapper();

    public boolean existsByLoginId(String loginId) {
        try {
            String sql = "SELECT COUNT(*) FROM admin WHERE login_id = ?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, loginId);
            return count > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException("아이디 중복 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void save(Admin admin) {
        try {
            String sql = "INSERT INTO admin (login_id, name, password, phone_number, sms_agree, sms_agreed_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

            Timestamp smsAgreedAt = null;
            if (admin.isSmsAgree() && admin.getSmsAgreedAt() != null) {
                smsAgreedAt = Timestamp.valueOf(LocalDateTime.parse(admin.getSmsAgreedAt()));
            }

            jdbcTemplate.update(sql,
                admin.getLoginId(),
                admin.getName(),
                admin.getPassword(),
                admin.getPhoneNumber(),
                admin.isSmsAgree(),
                smsAgreedAt
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("관리자 정보 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Admin findByLoginId(String loginId) {
        try {
            String sql = "SELECT * FROM admin WHERE login_id = ?";
            return jdbcTemplate.queryForObject(sql, adminRowMapper, loginId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException("관리자 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private static class AdminRowMapper implements RowMapper<Admin> {
        @Override
        public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Admin admin = new Admin();
            admin.setId(rs.getLong("id"));
            admin.setLoginId(rs.getString("login_id"));
            admin.setName(rs.getString("name"));
            admin.setPassword(rs.getString("password"));
            admin.setPhoneNumber(rs.getString("phone_number"));
            admin.setSmsAgree(rs.getBoolean("sms_agree"));
            
            Timestamp smsAgreedAt = rs.getTimestamp("sms_agreed_at");
            if (smsAgreedAt != null) {
                admin.setSmsAgreedAt(smsAgreedAt.toString());
            }
            
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                admin.setCreatedAt(createdAt.toString());
            }
            
            return admin;
        }
    }
} 