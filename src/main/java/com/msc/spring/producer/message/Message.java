package com.msc.spring.producer.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
public class Message implements Serializable {

    @Value("${message.notificationType}")
    private String messageType;

    @Value("message.text")
    private String messageText;
}