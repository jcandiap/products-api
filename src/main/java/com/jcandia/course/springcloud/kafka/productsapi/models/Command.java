package com.jcandia.course.springcloud.kafka.productsapi.models;

public record Command<T>(
        CommandType type,
        Long id,
        T body
) {
}
