package com.forescout.risk.service.servicebus;

import com.forescout.risk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QueueReceiveService {
    private static final String QUEUE_NAME = "techxus-forescout-risk-queue";

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(User user) {
        log.info("Received - {}:{}", user.getFirstName(), user.getLastName());
    }
}
