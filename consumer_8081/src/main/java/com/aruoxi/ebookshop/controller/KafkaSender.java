package com.aruoxi.ebookshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class KafkaSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);

	@Autowired
	//@Resource
	private KafkaTemplate<String, Long> kafkaTemplate;

	@Value("${spring.kafka.topic.name}")
	private String topicName;

	public void sendData(Long id) {
		// TODO Auto-generated method stub
//		Map<String, Object> headers = new HashMap<>();
//		headers.put(KafkaHeaders.TOPIC, topicName);
//		kafkaTemplate.send(new GenericMessage<String>(student.toString(), headers));
		// use the below to send String values through kafka
		this.kafkaTemplate.send(topicName, id);
		LOGGER.info("Data - " + id.toString() + " sent to Kafka Topic - " + topicName);
	}
}
