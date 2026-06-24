package com.jcandia.course.springcloud.kafka.productsapi.controllers;

import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;
import com.jcandia.course.springcloud.kafka.productsapi.services.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductCommandService commandService;

    public ProductController(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductDTO productDTO) {
        commandService.sendCreate(productDTO);
        return ResponseEntity
                .ok()
                .body(Map.of("message", "success sent"));
    }

}
