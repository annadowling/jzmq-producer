package com.msc.spring.producer.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by annadowling on 2020-01-16.
 */

@Configuration
public class Message {

    @Value("${message.notificationType}")
    public String messageType;

    @Value("message.volume")
    public int messageVolume;

    @Value("message.size.bytes")
    public int messageSizeBytes;
}