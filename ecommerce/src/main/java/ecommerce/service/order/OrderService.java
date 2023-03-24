package ecommerce.service.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import ecommerce.commons.kafka.KaftaConsumerService;
import ecommerce.commons.kafka.KaftaProducerService;
import ecommerce.commons.kafka.Message;
import ecommerce.commons.kafka.MessageDeserializer;
import ecommerce.commons.kafka.MessageSerializer;
import ecommerce.financial.FinancialService;
import ecommerce.fraud.detector.FraudDetectorService;

public class OrderService {

	Map<String, TransactionOrder> mapTransactionOrder = new HashMap<String, TransactionOrder>();	

	KaftaProducerService<String, Message<Order>> producerOrder = new KaftaProducerService<String, Message<Order>>(
			buildPropertiesProducerOrder());

	KaftaConsumerService<String, Message<Order>> consumerOrderNewProcessed = new KaftaConsumerService<String, Message<Order>>(
			"ECOMMERCE_ORDER_NEW_PROCESSED",this::processOrderNewProcessed, buildPropertiesConsumerFraud());

	KaftaConsumerService<String, Order> consumerOrderFraudError = new KaftaConsumerService<String, Order>(
			"ECOMMERCE_ORDER_NEW_ERROR", this::processOrderNewError, buildPropertiesConsumerFraud());
	

	public OrderService() {
		new Thread(() -> consumerOrderNewProcessed.process()).start();
		new Thread(() -> consumerOrderFraudError.process()).start();		
	}

	
	private List<String> toListIdtransactionOrder(ConsumerRecords<String, Order> records){
		List<String> ids = new ArrayList<>();
		Iterator<ConsumerRecord<String, Order>> it = records.iterator();
		while(it.hasNext()) {
			ids.add(it.next().value().id);
		}
		return ids;
	}	


	void processOrderNewProcessed(ConsumerRecords<String, Message<Order>> records) {
		System.out.println("PROCESSADA ORDER COM SUCESSO");
				
	}

	void processOrderNewError(ConsumerRecords<String, Order> records) {
		
	}
	
	public void receiverOrderNew(Order order) {

		try {
			var transaction = new TransactionOrder(order.id, order);			
			//orchestrationNewOrderManager.startNewOrchestration(order.id);
			Message<Order> message = new Message<Order>(order);
			message.appendCorrelationId(OrderService.class.getSimpleName());
			producerOrder.send("ECOMMERCE_ORDER_NEW", order.id, message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Properties buildPropertiesProducerOrder() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
		return properties;
	}

	private static Properties buildPropertiesConsumerFraud() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());		
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
		properties.setProperty(MessageDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}
	
	private Properties buildPropertiesConsumerFinancial() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FinancialService.class.getSimpleName());		
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FinancialService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
		properties.setProperty(MessageDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}

}
