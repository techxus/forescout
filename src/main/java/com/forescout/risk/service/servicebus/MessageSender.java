package com.forescout.risk.service.servicebus;

import com.forescout.risk.model.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    private static final String QUEUE_NAME = "techxus-forescout-risk-queue";

    @Value("${risk-queue-name}")
    private String queueName;

    // if Not using JMS
    // private final ServiceBusSenderClient senderClient;
    private final JmsTemplate jmsTemplate;

    @PostConstruct
    public void init() {
        log.info("queue name {}", queueName);
    }

    public void send(User user) {

        // if Not using JMS
        // senderClient.sendMessage(new com.azure.messaging.servicebus.ServiceBusMessage(message));
        jmsTemplate.convertAndSend(QUEUE_NAME, user);
        log.info("Message sent");
    }
}

