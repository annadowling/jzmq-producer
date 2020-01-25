package com.msc.spring.producer.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by annadowling on 2020-01-16.
 */

@Configuration
public class Message {

    @Value("${message.notificationType}")
    public String messageType;

    @Value("#{new Integer('${message.volume}')}")
    public Integer messageVolume;

    @Value("#{new Integer('${message.size.bytes}')}")
    public Integer messageSizeBytes;

    /**
     * Generate Message with Configurable Byte Size for send
     * @return String
     */
    public String generateMessage() {
        char[] chars = new char[messageSizeBytes];
        Arrays.fill(chars, 'T');
        return new String(chars);
    }
}