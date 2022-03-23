package com.monica.seckilldemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

//    public static final String exchange = "topic.e";
//
//    public static final String routing = "r";
//
//    public final static String queue = "topic.a";
//
//    public final static String queue01 = "queue01";
//
//    public final static String queue02 = "queue02";
//
//    public final static String fanout = "fanoutExchange";
//
//    //topic模式
//    @Bean
//    public Queue queue() {
//        //创建一个持久化的队列
//        return new Queue(RabbitMQConfig.queue, true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(RabbitMQConfig.exchange);
//    }
//
//    @Bean
//    public Binding binding(TopicExchange topicExchange, Queue queue) {
//        return BindingBuilder.bind(queue).to(topicExchange).with(RabbitMQConfig.routing);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        return new RabbitTemplate(connectionFactory);
//    }
//
//    //fanout模式
//    @Bean
//    public Queue queue01(){
//        return new Queue(RabbitMQConfig.queue01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(RabbitMQConfig.queue02);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(RabbitMQConfig.fanout);
//    }
//
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
//
//    public final static String queue03 = "queue03";
//
//    public final static String queue04 = "queue04";
//
//    public final static String direct = "directExchange";
//
//    public final static String key01 = "key.red";
//
//    public final static String key02 = "key.green";
//    //direct模式
//    @Bean
//    public Queue queue03(){
//        return new Queue(queue03);
//    }
//
//    @Bean
//    public Queue queue04(){
//        return new Queue(queue04);
//    }
//
//    @Bean
//    public DirectExchange directExchange(){
//        return new DirectExchange(direct);
//    }
//
//    @Bean
//    public Binding binding03(){
//        return BindingBuilder.bind(queue03()).to(directExchange()).with(key01);
//    }
//
//    @Bean
//    public Binding binding04(){
//        return BindingBuilder.bind(queue04()).to(directExchange()).with(key02);
//    }

    public final static String QUEUQ = "SeckillQueue";

    public final static String EXCHANGE = "SeckillExchange";

    @Bean
    public Queue queue(){
        return new Queue(QUEUQ);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }

}
