package com.msc.spring.producer.message;

import com.msc.spring.producer.jzmq.JZMQPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Created by annadowling on 2020-01-16.
 */

@Configuration
public class MessageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);

    @Value("${message.notificationType}")
    public String messageType;

    @Value("#{new Integer('${message.volume}')}")
    public Integer messageVolume;

    @Value("#{new Integer('${message.size.bytes}')}")
    public Integer messageSizeBytes;

    @Autowired
    MessageRepository repository;

    /**
     * Generate Message with Configurable Byte Size for send
     * @return String
     */
    public String generateMessage() {
        char[] chars = new char[messageSizeBytes];
        Arrays.fill(chars, 'T');
        return new String(chars);
    }

    /**
     * formatMessage into map containing correlationId, messageID and message
     * @param messageText
     * @return Map<String, String>
     */
    public Map<String, String> formatMessage(String messageText, String messageId){
        Map<String, String> messageMap = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        String correlationId = uuid.toString();

        messageMap.put("correlationId", correlationId);
        messageMap.put("messageId", messageId);
        messageMap.put("message", messageText);
        messageMap.put("messageVolume", messageVolume.toString());
        messageMap.put("messageSize", messageSizeBytes.toString());

        return messageMap;
    }

    /**
     * Save a message entry to the db for each sent message
     * @param messageMap
     */
    public void saveMessage(Map<String, String> messageMap, boolean isMultiThreaded){
        Message message = new Message();
        message.setCorrelationId(messageMap.get("correlationId"));
        message.setRequestType(messageMap.get("messageId"));
        message.setMessageVolume(messageVolume);
        message.setMessageSize(messageSizeBytes);
        message.setSendTime(new Date());
        message.setMultiThreaded(isMultiThreaded);

        repository.save(message);
    }

    /**
     * Convert Map to Byte Array for use with message sending requiring bytes
     *
     * @param messageMap
     * @return byte[]
     */
    public byte[] convertMapToBytes(Map<String, String> messageMap) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(messageMap);
            out.flush();
        }catch(IOException ex){
            LOGGER.info("IOException = " + ex);
        } finally {
            try {
                byteOut.close();
            } catch (IOException ex) {
                LOGGER.info("IOException = " + ex);
            }
        }
        return byteOut.toByteArray();
    }
}