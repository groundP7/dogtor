package com.ground.dogtor.domain.admin.controller;

import com.ground.dogtor.domain.admin.dto.AdminLoginRequest;
import com.ground.dogtor.domain.admin.dto.AdminLoginResponse;
import com.ground.dogtor.domain.admin.dto.AdminSignUpRequest;
import com.ground.dogtor.domain.admin.entity.Admin;
import com.ground.dogtor.domain.admin.service.AdminService;
import com.ground.dogtor.domain.product.entity.Product;
import com.ground.dogtor.domain.product.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    // 관리자 회원가입 페이지
    @GetMapping("/signup")
    public String signUpPage() {
        return "adminSignUp";
    }

    // 관리자 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> signUp(@ModelAttribute AdminSignUpRequest request) {
        try {
            adminService.signUp(request);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // 관리자 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "adminLogin";
    }

    // 관리자 로그인
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(
            @ModelAttribute AdminLoginRequest request,
            HttpSession session,
            HttpServletResponse response) {
        try {
            AdminLoginResponse loginResponse = adminService.login(request);
            
            // 세션에 필요한 정보만 저장
            session.setAttribute("adminId", loginResponse.getAdminId());
            session.setAttribute("adminName", loginResponse.getName());
            
            // Access Token을 쿠키에 저장
            Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(3600); // 1시간
            response.addCookie(accessTokenCookie);
            
            // Refresh Token을 쿠키에 저장
            Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 3600); // 7일
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // 관리자 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 세션 무효화
        session.invalidate();
        
        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
        
        return "redirect:/admin/login";
    }

    // 관리자 로그아웃
    @GetMapping("")
    public String dashboard(HttpSession session, Model model) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return "redirect:/admin/login";
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("foodProducts", products.stream()
                .filter(p -> "FOOD".equals(p.getCategory())).count());
        model.addAttribute("supplementProducts", products.stream()
                .filter(p -> "SUPPLEMENT".equals(p.getCategory())).count());
        model.addAttribute("lowStockProducts", products.stream()
                .filter(p -> p.getStock() < 10).count());

        return "adminDashboard";
    }

    @GetMapping("/products/new")
    public String newProductForm(HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return "redirect:/admin/login";
        }
        return "adminProductForm";
    }

    // 제품 등록
    @PostMapping("/products/new")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createProduct(
            @ModelAttribute Product product,
            @RequestParam("image") MultipartFile image,
            HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "로그인이 필요합니다."
            ));
        }

        try {
            productService.createProduct(product, image);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // 제품 수정 페이지
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, HttpSession session, Model model) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return "redirect:/admin/login";
        }

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "adminProductForm";
    }

    // 제품 수정
    @PostMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "로그인이 필요합니다."
            ));
        }

        try {
            product.setId(id);
            productService.updateProduct(product, image);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // 제품 삭제
    @DeleteMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @PathVariable Long id,
            HttpSession session) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "로그인이 필요합니다."
            ));
        }

        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
} 