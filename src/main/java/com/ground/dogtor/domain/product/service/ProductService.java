package com.ground.dogtor.domain.product.service;

import com.ground.dogtor.domain.product.dao.ProductDAO;
import com.ground.dogtor.domain.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public void createProduct(Product product, MultipartFile image) throws IOException {
        validateProduct(product);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            product.setImageUrl(imageUrl);
        }

        productDAO.save(product);
    }

    @Transactional
    public void updateProduct(Product product, MultipartFile image) throws IOException {
        validateProduct(product);

        Product existingProduct = productDAO.findById(product.getId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }

        if (image != null && !image.isEmpty()) {
            // 기존 이미지 삭제
            if (existingProduct.getImageUrl() != null) {
                deleteImage(existingProduct.getImageUrl());
            }
            // 새 이미지 저장
            String imageUrl = saveImage(image);
            product.setImageUrl(imageUrl);
        } else {
            product.setImageUrl(existingProduct.getImageUrl());
        }

        productDAO.update(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }

        if (product.getImageUrl() != null) {
            deleteImage(product.getImageUrl());
        }

        productDAO.delete(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product getProductById(Long id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }
        return product;
    }

    private void validateProduct(Product product) {
        if (!StringUtils.hasText(product.getName())) {
            throw new IllegalArgumentException("제품명은 필수입니다.");
        }
        if (!StringUtils.hasText(product.getCategory())) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }
        if (!StringUtils.hasText(product.getDescription())) {
            throw new IllegalArgumentException("제품 설명은 필수입니다.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        // 업로드 디렉토리 생성
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 파일명 생성
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // 파일 저장
        Path path = Paths.get(uploadPath, filename);
        Files.write(path, image.getBytes());

        return "/uploads/" + filename;
    }

    private void deleteImage(String imageUrl) {
        try {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path path = Paths.get(uploadPath, filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // 파일 삭제 실패는 무시
        }
    }
} 