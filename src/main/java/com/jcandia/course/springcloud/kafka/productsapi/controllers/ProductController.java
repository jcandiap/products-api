package com.jcandia.course.springcloud.kafka.productsapi.controllers;

import com.jcandia.course.springcloud.kafka.productsapi.models.Reply;
import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;
import com.jcandia.course.springcloud.kafka.productsapi.services.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
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
        Reply<?> reply = commandService.sendCreateAndAwait(productDTO, Duration.ofSeconds(5));

        if( "SUCCESS".equalsIgnoreCase(reply.status()) ) {
            return ResponseEntity.ok(reply.body());
        }

        return ResponseEntity.badRequest().body(Map.of("error", reply.message()));
    }

}
