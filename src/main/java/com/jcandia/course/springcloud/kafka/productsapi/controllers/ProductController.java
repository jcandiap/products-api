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
        return getResponseEntity(commandService.sendCreateAndAwait(productDTO, Duration.ofSeconds(5)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return getResponseEntity(commandService.sendReadAndAwait(id, Duration.ofSeconds(5)));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return getResponseEntity(commandService.sendReadAllAndAwait(Duration.ofSeconds(5)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        return getResponseEntity(commandService.sendUpdateAndAwait(productDTO, id, Duration.ofSeconds(5)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return getResponseEntity(commandService.sendDeleteAndAwait(id, Duration.ofSeconds(10)));
    }

    private static @NonNull ResponseEntity<?> getResponseEntity(Reply<?> reply) {
        if( "SUCCESS".equalsIgnoreCase(reply.status()) ) {
            return ResponseEntity.ok(reply.body());
        }

        return ResponseEntity.badRequest().body(Map.of("error", reply.message()));
    }

}
