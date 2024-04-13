package com.forescout.risk.config.servicebus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forescout.risk.model.User;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.*;
import java.io.IOException;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public MessageConverter myMessageConverter() {
        return new SimpleMessageConverter() {
            @Override
            public Object fromMessage(Message message) throws JMSException, MessageConversionException {
                try {
                    String jsonText = ((TextMessage) message).getText();
                    return new ObjectMapper().readValue(jsonText, User.class);
                } catch (IOException e) {
                    throw new MessageConversionException("Failed to read JSON", e);
                }
            }
        };
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                          MessageConverter converter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(converter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}

