package com.aruoxi.ebookshop.service.impl;

import com.aruoxi.ebookshop.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaReciever {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReciever.class);

	@Resource
	BookService bookService;

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void recieveData(Long id) {
		LOGGER.info("Data - " + id.toString() + " recieved");
		bookService.delete(id);
		//LOGGER.info("Data - " + student + " recieved");
//		System.out.println("Received Message - student id : " + student.getCustomerId());
//		System.out.println("Received Message - student name: " + student.getFirstName() + " " + student.getLastName());
       // currentList.add(student);

 	}

//	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
//	public void service(customerDto customerdto)
//			throws ServletException, IOException {
//		customerdto.getRequest().setAttribute("customer",customerdto.getCustomer());
//		customerdto.getRequest().getRequestDispatcher("localhost:8081/kafkaProducer");
//		request.setCharacterEncoding("utf-8");
//
//		response.setContentType("text/html;charset=utf-8");
//
//		PrintWriter out = response.getWriter();
//
//		String param = request.getParameter("param");//获取参数
//
////你的操作
//
////返回数据
//
//		String json = "{\"id\":1,\"name\":\"张三\",\"age\":18}";
//
//		out.print(json);
//	}

	
//	@KafkaListener(topicPartitions = @TopicPartition(topic = "${spring.kafka.topic.name}", partitionOffsets = {
//		    @PartitionOffset(partition = "0", initialOffset = "0") }), containerFactory = "kafkaListenerContainerFactory",
//		    groupId = "group-2")
//	public void addtoList(Customer student) {
//		LOGGER.info("Data - " + student.toString() + " recieved");
//		//LOGGER.info("Data - " + student + " recieved");
//		System.out.println("Received Message - student id : " + student.getCustomerId());
//		System.out.println("Received Message - student name: " + student.getFirstName() + " " + student.getLastName());
//        allList.add(student);
//	}
//
//	public List<Customer> getCurrent() {
//		return currentList;
//	}
//
//	public List<Customer> getAll() {
//		return allList;
//	}

}
