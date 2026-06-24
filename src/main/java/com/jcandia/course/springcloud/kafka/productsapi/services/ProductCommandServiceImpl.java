package com.jcandia.course.springcloud.kafka.productsapi.services;

import com.jcandia.course.springcloud.kafka.productsapi.models.Command;
import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge bridge;

    public ProductCommandServiceImpl(StreamBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void sendCreate(ProductDTO productDTO) {
        Command<ProductDTO> cmd = new Command<>("CREATE", null, productDTO);
        boolean isSent = this.bridge.send("commands-out-0", cmd);

        if( !isSent ) {
            throw  new IllegalStateException("No se pudo enviar el comando a kafka.");
        }
    }

}
