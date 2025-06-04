package com.ground.dogtor.domain.category.controller;

import com.ground.dogtor.domain.product.entity.Product;
import com.ground.dogtor.domain.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{category}")
    public String categoryProducts(@PathVariable String category, Model model) {
        List<Product> products = productService.getProductsByCategory(category.toUpperCase());
        model.addAttribute("products", products);
        model.addAttribute("categoryName", category.equals("food") ? "사료" : "영양제");
        return "categoryProducts";
    }
} 