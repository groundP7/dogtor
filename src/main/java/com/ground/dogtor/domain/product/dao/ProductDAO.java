package com.ground.dogtor.domain.product.dao;

import com.ground.dogtor.domain.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productRowMapper = new ProductRowMapper();

    public void save(Product product) {
        try {
            String sql = "INSERT INTO product (name, category, description, price, stock, image_url) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setString(2, product.getCategory());
                ps.setString(3, product.getDescription());
                ps.setInt(4, product.getPrice());
                ps.setInt(5, product.getStock());
                ps.setString(6, product.getImageUrl());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                product.setId(key.longValue());
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("제품 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void update(Product product) {
        try {
            String sql = "UPDATE product SET name = ?, category = ?, description = ?, " +
                        "price = ?, stock = ?, image_url = ? WHERE id = ?";

            int updatedRows = jdbcTemplate.update(sql,
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getId()
            );

            if (updatedRows != 1) {
                throw new RuntimeException("제품 수정에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("제품 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            String sql = "DELETE FROM product WHERE id = ?";
            int updatedRows = jdbcTemplate.update(sql, id);

            if (updatedRows != 1) {
                throw new RuntimeException("제품 삭제에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("제품 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public Product findById(Long id) {
        try {
            String sql = "SELECT * FROM product WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, productRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException("제품 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public List<Product> findAll() {
        try {
            String sql = "SELECT * FROM product ORDER BY id DESC";
            return jdbcTemplate.query(sql, productRowMapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("제품 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getInt("price"));
            product.setStock(rs.getInt("stock"));
            product.setImageUrl(rs.getString("image_url"));
            
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                product.setCreatedAt(createdAt.toString());
            }
            
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                product.setUpdatedAt(updatedAt.toString());
            }
            
            return product;
        }
    }
} 