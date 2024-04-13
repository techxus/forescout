package com.forescout.risk;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RiskShareService {

    public static void main(String[] args) {

        SpringApplication.run(RiskShareService.class, args);

        /* if Not using JMS
        ConfigurableApplicationContext context = SpringApplication.run(RiskShareService.class, args);
        ServiceBusProcessorClient processorClient = context.getBean(ServiceBusProcessorClient.class);
        processorClient.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            processorClient.close();
            context.close();
        }));
        */
    }
}
