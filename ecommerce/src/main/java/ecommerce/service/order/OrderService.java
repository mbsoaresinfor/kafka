package ecommerce.service.order;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import ecommerce.commons.kafka.KaftaConsumerService;
import ecommerce.commons.kafka.KaftaProducerService;
import ecommerce.commons.kafka.OrderDeserializer;
import ecommerce.commons.kafka.OrderSerializer;
import ecommerce.fraud.detector.FraudDetectorService;

public class OrderService {

	Map<String, TransactionOrder> mapTransaction = new HashMap<String,TransactionOrder>();
	
	
	KaftaProducerService<String, Order> producerOrder = new KaftaProducerService<String, Order>(
			buildPropertiesProducerOrder());
	
	KaftaConsumerService<String, Order> consumerOrderFraudOk = 
				new KaftaConsumerService<String, Order>("ORDER_FRAUD_OK",
			this::processOrderFraudOk, buildPropertiesConsumerFraud());
	
	KaftaConsumerService<String, Order> consumerOrderFraudError = 
			new KaftaConsumerService<String, Order>("ORDER_FRAUD_ERROR",
		this::processOrderFraudError, buildPropertiesConsumerFraud());
	
	public OrderService() {		
		new Thread(() -> consumerOrderFraudOk.process()).start();
		new Thread(() -> consumerOrderFraudError.process()).start();
	}
	
	void processOrderFraudOk(ConsumerRecords<String, Order> records) {
		
		for(var record : records) {			
			TransactionOrder transactionOrder = mapTransaction.get(record.value().id);
			if(transactionOrder != null) {
				System.out.println("Processing order fraud ok, order id: "+ record.value().id);
				StageStatus stageStatus = new StageStatus(true, true);
				transactionOrder.dectectorFraud = stageStatus;
				orchestrationOrderNew(transactionOrder);
			}
		}
		
	}
	
	void processOrderFraudError(ConsumerRecords<String, Order> records) {
		
		for(var record : records) {
			System.out.println("Processing order fraud error, order id: "+ record.value().id);
			TransactionOrder transactionOrder = mapTransaction.get(record.value().id);
			StageStatus stageStatus = new StageStatus(true, false);
			transactionOrder.dectectorFraud = stageStatus;
			orchestrationOrderNew(transactionOrder);
		}
	}
	
	void  orchestrationOrderNew(TransactionOrder transactionOrder) {
		System.out.println("Processing ORCHESTRATIN ORDER NEW");
		
	}
	
	public void receiverOrderNew(Order order) {
		
		try {
			var transaction = new TransactionOrder(order.id,order);
			mapTransaction.put(transaction.id,transaction);
			producerOrder.send("ORDER_NEW", transaction.id, order);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Properties buildPropertiesProducerOrder() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
		return properties;
	}
	
	private static Properties buildPropertiesConsumerFraud() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
		UUID.randomUUID();
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
		properties.setProperty(OrderDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}
	
	
}
