package com.ground.dogtor.domain.member.dao;

import com.ground.dogtor.domain.member.dto.MemberLoginRequest;
import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import com.ground.dogtor.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class MemberDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int existsByLoginId(String loginId) {
        try {
            String sql = "SELECT COUNT(*) FROM member WHERE login_id = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, loginId);
        } catch (DataAccessException e) {
            throw new RuntimeException("아이디 중복 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public long signUpMember(MemberSignUpRequest memberSignUpRequest) {
        try {
            String sql = "INSERT INTO member (login_id, name, password, phone_number, sms_agree, sms_agreed_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, memberSignUpRequest.getLoginId());
                ps.setString(2, memberSignUpRequest.getName());
                ps.setString(3, memberSignUpRequest.getPassword());
                ps.setString(4, memberSignUpRequest.getPhoneNumber());
                ps.setBoolean(5, memberSignUpRequest.isSmsAgree());

                if (memberSignUpRequest.isSmsAgree()) {
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    ps.setNull(6, Types.TIMESTAMP);
                }

                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new RuntimeException("회원 정보 저장 중 오류가 발생했습니다: 생성된 ID를 가져올 수 없습니다.");
            }
            return key.longValue();
        } catch (DataAccessException e) {
            throw new RuntimeException("회원 정보 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void signUpAddress(long memberId, MemberSignUpRequest memberSignUpRequest) {
        try {
            String sql = "INSERT INTO member_address " +
                        "(member_id, recipient_name, recipient_number, postal_code, address, detail_address, is_default) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

            int updatedRows = jdbcTemplate.update(sql,
                    memberId,
                    memberSignUpRequest.getName(),
                    memberSignUpRequest.getPhoneNumber(),
                    memberSignUpRequest.getPostalCode(),
                    memberSignUpRequest.getAddress(),
                    memberSignUpRequest.getDetailAddress(),
                    true
            );

            if (updatedRows != 1) {
                throw new RuntimeException("주소 정보 저장에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("주소 정보 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Member findByLoginId(MemberLoginRequest memberLoginRequest) {
        try {
            String sql = "SELECT * FROM member WHERE login_id = ?";
            return jdbcTemplate.queryForObject(sql, new MemberRowMapper(), memberLoginRequest.getLoginId());
        } catch (EmptyResultDataAccessException e) {
            return null; // 해당 아이디를 가진 회원이 없는 경우
        } catch (DataAccessException e) {
            throw new RuntimeException("회원 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("login_id"));
            member.setName(rs.getString("name"));
            member.setPassword(rs.getString("password"));
            member.setPhoneNumber(rs.getString("phone_number"));
            member.setSmsAgree(rs.getBoolean("sms_agree"));
            
            Timestamp smsAgreedAt = rs.getTimestamp("sms_agreed_at");
            if (smsAgreedAt != null) {
                member.setSmsAgreedAt(smsAgreedAt.toString());
            }
            
            return member;
        }
    }

}
