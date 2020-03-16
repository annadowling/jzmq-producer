package com.msc.spring.producer.jzmq;

import com.msc.spring.producer.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Map;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "jzmq", name = "enabled", havingValue = "true")
public class JZMQPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JZMQPublisher.class);

    @Value("${zeromq.address}")
    private String bindAddress;

    @Value("${jzmq.enabled}")
    private boolean jzmqEnabled;

    @Value("${multi.thread.enabled}")
    private boolean multiThreaded;

    final String errorMessage = "Exception encountered = ";

    @Autowired
    private MessageUtils messageUtils;

    @Bean
    public void setUpJZMQProducerAndSendMessage() {
        if (jzmqEnabled) {
            try (ZMQ.Context context = ZMQ.context(1);
                 ZMQ.Socket publisher = context.socket(ZMQ.PUB);
            ) {
                publisher.bind(bindAddress);
                LOGGER.info("Starting Publisher..");
                publisher.setIdentity("B".getBytes());
                // for testing setting sleep at 100ms to ensure started.
                Thread.sleep(100l);
                Integer messageVolume = messageUtils.messageVolume;

                for (int i = 1; i <= messageVolume; i++) {
                    publisher.sendMore("B");

                    String messageText = messageUtils.generateMessage();
                    Map<String, String> messageMap = messageUtils.formatMessage(messageText, "JZMQ");
                    messageUtils.saveMessage(messageMap, multiThreaded);

                    byte[] mapBytes = messageUtils.convertMapToBytes(messageMap);
                    ZMsg req = new ZMsg();
                    req.add(mapBytes);
                    LOGGER.info("Sending JZMQ Message " + i);
                    if (multiThreaded) {
                        sendMessageMultiThread(req, publisher);
                    } else {
                        req.send(publisher);
                    }
                }
                LOGGER.info("Completed sending all JZMQ Messages ");
                publisher.close();
                context.term();
            } catch (Exception e) {
                LOGGER.info(errorMessage + e.getLocalizedMessage());

            }
        }
    }

    @Async
    void sendMessageMultiThread(ZMsg req, ZMQ.Socket publisher) throws Exception {
        req.send(publisher);
    }
}
