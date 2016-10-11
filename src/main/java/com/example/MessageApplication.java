package com.example;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessageApplication {
	
	@Value("${queue}")
	private String queue;
	
	@Value("${exchange}")
	private String exchange;
	
	@Value("${routingKey}")
	private String routingKey;

	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
	}
	
	@Bean
	public Queue queue(){
		return new Queue(queue);
	}
	
	@Bean
	public TopicExchange exchange(){
		return new TopicExchange(exchange);
	}
	
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with(routingKey);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		return new RabbitTemplate(connectionFactory);
	}
}
