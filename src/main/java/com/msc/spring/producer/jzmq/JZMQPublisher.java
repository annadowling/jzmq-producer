package com.msc.spring.producer.jzmq;

import com.msc.spring.producer.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import java.util.Arrays;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "jzmq", name = "enabled", havingValue = "true")
public class JZMQPublisher {

    @Value("${zeromq.address}")
    private String bindAddress;

    @Value("${message.volume}")
    private int messageVolume;

    @Value("${jzmq.enabled}")
    private boolean jzmqEnabled;

    final String errorMessage = "Exception encountered = ";

    @Autowired
    private Message message;

    @Bean
    public void setUpJZMQProducerAndSendMessage() {
        if (jzmqEnabled) {
            try (ZMQ.Context context = ZMQ.context(1);
                 ZMQ.Socket publisher = context.socket(ZMQ.PUB);
            ) {
                publisher.bind(bindAddress);
                System.out.println("Starting Publisher..");
                publisher.setIdentity("B".getBytes());
                // for testing setting sleep at 100ms to ensure started.
                Thread.sleep(100l);
                for (int i = 0; i <= message.messageVolume; i++) {
                    publisher.sendMore("B");
                    boolean isSent = publisher.send("X(" + System.currentTimeMillis() + "):" + i + generateMessage());
                    System.out.println("JZMQ Message was sent " + i + " , " + isSent);
                }
                publisher.close();
                context.term();
            } catch (Exception e) {
                System.out.println(errorMessage + e.getLocalizedMessage());

            }
        }
    }

    public String generateMessage() {
        char[] chars = new char[message.messageSizeBytes];
        Arrays.fill(chars, 'T');
        return new String(chars);
    }
}
