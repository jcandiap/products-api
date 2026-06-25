package com.jcandia.course.springcloud.kafka.productsapi.services;

import com.jcandia.course.springcloud.kafka.productsapi.models.Reply;
import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;

import java.time.Duration;

public interface ProductCommandService {

    Reply<?> sendCreateAndAwait(ProductDTO productDTO, Duration timeout);
    Reply<?> sendReadAndAwait(Long id, Duration timeout);
    Reply<?> sendReadAllAndAwait(Duration timeout);
    Reply<?> sendUpdateAndAwait(ProductDTO productDTO, Long id, Duration timeout);

}
