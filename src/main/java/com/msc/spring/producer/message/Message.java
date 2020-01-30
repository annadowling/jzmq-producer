package com.msc.spring.producer.message;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String requestType;
    private String correlationId;

    @Column(nullable = true)
    private Date sendTime;

    private int messageVolume;
    private int messageSize;

    public Message() {
    }

    public int getMessageVolume() {
        return messageVolume;
    }

    public void setMessageVolume(int messageVolume) {
        this.messageVolume = messageVolume;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public java.util.Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(java.util.Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", requestType='" + requestType + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", sendTime=" + sendTime +
                ", messageVolume=" + messageVolume +
                ", messageSize=" + messageSize +
                '}';
    }
}
