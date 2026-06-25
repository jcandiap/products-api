package com.jcandia.course.springcloud.kafka.productsapi.controllers;

import com.jcandia.course.springcloud.kafka.productsapi.models.Reply;
import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;
import com.jcandia.course.springcloud.kafka.productsapi.services.ProductCommandService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return getResponseEntity(reply);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Reply<?> reply = commandService.sendReadAndAwait(id, Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        Reply<?> reply = commandService.sendReadAllAndAwait(Duration.ofSeconds(5));
        return getResponseEntity(reply);
    }

    private static @NonNull ResponseEntity<?> getResponseEntity(Reply<?> reply) {
        if( "SUCCESS".equalsIgnoreCase(reply.status()) ) {
            return ResponseEntity.ok(reply.body());
        }

        return ResponseEntity.badRequest().body(Map.of("error", reply.message()));
    }

}
