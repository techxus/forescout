package com.forescout.risk.config.servicebus;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// if Not using JMS
//@Configuration
@Slf4j
public class AzureServiceBusConfig {

    //@Value("${azure.servicebus.connection-string}")
    private String connectionString;

    //@Value("${azure.servicebus.queue-name}")
    private String queueName;

    //@Bean
    public ServiceBusSenderClient serviceBusSenderClient() {
        return new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
    }

    //@Bean
    public ServiceBusProcessorClient serviceBusProcessorClient() {
        return new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .queueName(queueName)
                .processMessage(context -> {
                    log.info("Received: " + context.getMessage().getBody());
                })
                .processError(context -> {
                    log.error("Error occurred: " + context.getException());
                })
                .buildProcessorClient();
    }
}

