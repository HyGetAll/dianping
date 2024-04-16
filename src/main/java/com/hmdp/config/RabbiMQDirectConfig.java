package com.hmdp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbiMQDirectConfig {
    
    public static final String EXCHANGE_NAME="seckilExchange";
    public static final String QUEUE_NAME="seckilQueue";
    public static final String BINDING_KEY="seckil";

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding(Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(BINDING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
