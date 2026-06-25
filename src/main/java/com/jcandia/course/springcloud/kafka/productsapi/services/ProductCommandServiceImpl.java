package com.jcandia.course.springcloud.kafka.productsapi.services;

import com.jcandia.course.springcloud.kafka.productsapi.messaging.ReplyInbox;
import com.jcandia.course.springcloud.kafka.productsapi.models.Command;
import com.jcandia.course.springcloud.kafka.productsapi.models.Reply;
import com.jcandia.course.springcloud.kafka.productsapi.models.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge bridge;
    private final ReplyInbox replyInbox;
    private static final Logger logger = LoggerFactory.getLogger(ProductCommandServiceImpl.class);

    public ProductCommandServiceImpl(StreamBridge bridge, ReplyInbox replyInbox) {
        this.bridge = bridge;
        this.replyInbox = replyInbox;
    }

    @Override
    public Reply<?> sendCreateAndAwait(ProductDTO productDTO, Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("CREATE", null, productDTO);
        return sendAndAwait(cmd, timeout);
    }

    @Override
    public Reply<?> sendReadAndAwait(Long id, Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("FIND", id, null);
        return sendAndAwait(cmd, timeout);
    }

    @Override
    public Reply<?> sendReadAllAndAwait(Duration timeout) {
        Command<Object> cmd = new Command<>("FIND_ALL", null, null);
        return sendAndAwait(cmd, timeout);
    }

    @Override
    public Reply<?> sendUpdateAndAwait(ProductDTO productDTO, Long id, Duration timeout) {
        Command<ProductDTO> cmd = new Command<>("UPDATE", id, productDTO);
        return sendAndAwait(cmd, timeout);
    }

    @Override
    public Reply<?> sendDeleteAndAwait(Long id, Duration timeout) {
        Command<Object> cmd = new Command<>("DELETE", id, null);
        return sendAndAwait(cmd, timeout);
    }

    private Reply<?> sendAndAwait(Command<?> cmd, Duration timeout) {
        String correlationId = UUID.randomUUID().toString();

        logger.info("[API PRODUCTS] Creating product with correlationId {}", correlationId);

        CompletableFuture<Reply<?>> future = replyInbox.register(correlationId);

        var message = MessageBuilder.withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        boolean isSent = this.bridge.send("commands-out-0", message);

        if( !isSent ) {
            throw  new IllegalStateException("No se pudo enviar el comando a kafka.");
        }

        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Timeout waiting the products-commands response from Kafka");
        }
    }

}
