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
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> memberRowMapper = new MemberRowMapper();

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

    public void updateRefreshToken(Long memberId, String refreshToken) {
        try {
            String sql = "UPDATE member SET refresh_token = ? WHERE id = ?";
            int updatedRows = jdbcTemplate.update(sql, refreshToken, memberId);
            
            if (updatedRows != 1) {
                throw new RuntimeException("Refresh Token 업데이트에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Refresh Token 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Member findByNameAndPhoneNumber(String name, String phoneNumber) {
        String sql = "SELECT * FROM member WHERE name = ? AND phone_number = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, name, phoneNumber);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Member findByLoginIdAndPhoneNumber(String loginId, String phoneNumber) {
        String sql = "SELECT * FROM member WHERE login_id = ? AND phone_number = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, loginId, phoneNumber);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updatePassword(Long memberId, String encodedPassword) {
        String sql = "UPDATE member SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, encodedPassword, memberId);
    }

    public Member findById(Long memberId) {
        try {
            String sql = "SELECT * FROM member WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, memberRowMapper, memberId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException("회원 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void updateProfile(Long memberId, String name, String phoneNumber, String encodedPassword) {
        try {
            StringBuilder sql = new StringBuilder("UPDATE member SET name = ?, phone_number = ?");
            
            if (encodedPassword != null) {
                sql.append(", password = ?");
            }
            
            sql.append(" WHERE id = ?");

            if (encodedPassword != null) {
                jdbcTemplate.update(sql.toString(), name, phoneNumber, encodedPassword, memberId);
            } else {
                jdbcTemplate.update(sql.toString(), name, phoneNumber, memberId);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Map<String, String> findAddressByMemberId(Long memberId) {
        try {
            String sql = "SELECT postal_code, address, detail_address FROM member_address WHERE member_id = ? AND is_default = true";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Map<String, String> address = new HashMap<>();
                address.put("postalCode", rs.getString("postal_code"));
                address.put("address", rs.getString("address"));
                address.put("detailAddress", rs.getString("detail_address"));
                return address;
            }, memberId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException("주소 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void updateAddress(Long memberId, String postalCode, String address, String detailAddress) {
        try {
            String sql = "UPDATE member_address SET postal_code = ?, address = ?, detail_address = ? " +
                        "WHERE member_id = ? AND is_default = true";
            
            int updatedRows = jdbcTemplate.update(sql, postalCode, address, detailAddress, memberId);
            
            if (updatedRows == 0) {
                // 기본 주소가 없는 경우 새로 추가
                sql = "INSERT INTO member_address (member_id, postal_code, address, detail_address, is_default) " +
                      "VALUES (?, ?, ?, ?, true)";
                jdbcTemplate.update(sql, memberId, postalCode, address, detailAddress);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("주소 수정 중 오류가 발생했습니다: " + e.getMessage());
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
