package com.example;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
	
	@Value("${queue}")
	private String queue;
	
	@Value("${exchange}")
	private String exchange;
	
	@Value("${routingKey}")
	private String routingKey;

	private Logger logger = Logger.getLogger(MessageController.class);

	private RabbitTemplate rabbitTemplate;
	
	
	public MessageController(RabbitTemplate rabbitTemplate) {
		// TODO Auto-generated constructor stub
		this.rabbitTemplate = rabbitTemplate;
	}
	
	@RequestMapping(value="/publish", method=RequestMethod.POST)
	public String publish(String message){
		logger.info("Sending Message: " + message);
		rabbitTemplate.setExchange(exchange);
		rabbitTemplate.setRoutingKey(routingKey);
		rabbitTemplate.convertSendAndReceive(message);
		return message;
	}

	@RequestMapping(value="/receive", method=RequestMethod.GET)
	public String receive(){
		logger.info("Reading message from queue: " + queue);
		String message = (String) rabbitTemplate.receiveAndConvert(queue);
		if(message == null){
			message = "Oh snap, no more messages in the queue: " + queue;
			logger.info("Empty queue: " + queue);
		}
		else{
			logger.info("Read message: " + message);
		}
		return message;
	}
}
