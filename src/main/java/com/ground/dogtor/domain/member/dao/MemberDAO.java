package com.ground.dogtor.domain.member.dao;

import com.ground.dogtor.domain.member.dto.MemberSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

@Repository
public class MemberDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int existsByLoginId(String loginId) {
        String sql = "SELECT COUNT(*) FROM member WHERE login_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, loginId);
    }

    public long signUpMember(MemberSignUpRequest memberSignUpRequest) {
        String sql = "INSERT INTO member (login_id, name, password, phone_number, sms_agree, sms_agreed_at) VALUES ( ?, ?, ?, ?, ?, ?)";
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

        return keyHolder.getKey().longValue();
    }

    public void signUpAddress(long memberId, MemberSignUpRequest memberSignUpRequest){
        String sql = "INSERT INTO member_address (member_id, recipient_name, recipient_number, postal_code, address, detail_address, is_default) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                memberId,
                memberSignUpRequest.getName(),
                memberSignUpRequest.getPhoneNumber(),
                memberSignUpRequest.getPostalCode(),
                memberSignUpRequest.getAddress(),
                memberSignUpRequest.getDetailAddress(),
                true
        );
    }
}
