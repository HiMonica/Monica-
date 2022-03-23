package com.monica.seckilldemo.rabbitmq;

import org.springframework.amqp.rabbit.connection.CorrelationData;

public class MQCorrelationData extends CorrelationData {
    //数据
    private volatile Object data;
    //队列
    private String queueName;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

}
