package com.forescout.risk.controller;

import com.forescout.risk.model.User;
import com.forescout.risk.service.servicebus.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forescout/eventhub")
@RequiredArgsConstructor
@Slf4j
public class EventHubController {

    private final MessageSender messageSender;

    @GetMapping("/")
    @SneakyThrows
    public ResponseEntity<String> getMessageStatus()
    {
        messageSender.send(User.builder()
                .firstName("Daniel")
                .lastName("Acquah")
                .build());
        return new ResponseEntity<>("Message Sent", HttpStatus.OK);
    }
}
