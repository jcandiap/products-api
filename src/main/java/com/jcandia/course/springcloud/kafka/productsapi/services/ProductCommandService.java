package com.jcandia.course.springcloud.kafka.productsapi.services;

import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;

public interface ProductCommandService {

    void sendCreate(ProductDTO productDTO);

}
