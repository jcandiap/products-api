package com.jcandia.course.springcloud.kafka.productsapi.models;

public record Command<T>(
        String type,
        Long id,
        T body
) {
}
