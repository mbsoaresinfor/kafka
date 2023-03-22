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

	KaftaConsumerService<String, Order> consumerOrderFraudOk = new KaftaConsumerService<String, Order>(
			"ORDER_FRAUD_OK",this::processOrderFraudServiceOk, buildPropertiesConsumerFraud());

	KaftaConsumerService<String, Order> consumerOrderFraudError = new KaftaConsumerService<String, Order>(
			"ORDER_FRAUD_ERROR", this::processOrderFraudServiceError, buildPropertiesConsumerFraud());
	
	KaftaConsumerService<String, Order> consumerOrderFinancialError = new KaftaConsumerService<String, Order>(
			"ORDER_FINANCIAL_ERROR", this::processOrderFinancialServiceError, buildPropertiesConsumerFinancial());

	KaftaConsumerService<String, Order> consumerOrderFinancialOk = new KaftaConsumerService<String, Order>(
			"ORDER_FINANCIAL_OK", this::processOrderFinancialServiceOk, buildPropertiesConsumerFinancial());

	

	public OrderService() {
		new Thread(() -> consumerOrderFraudOk.process()).start();
		new Thread(() -> consumerOrderFraudError.process()).start();
		new Thread(() -> consumerOrderFinancialOk.process()).start();
		new Thread(() -> consumerOrderFinancialError.process()).start();
	}

	
	private List<String> toListIdtransactionOrder(ConsumerRecords<String, Order> records){
		List<String> ids = new ArrayList<>();
		Iterator<ConsumerRecord<String, Order>> it = records.iterator();
		while(it.hasNext()) {
			ids.add(it.next().value().id);
		}
		return ids;
	}	
	
	void processOrderFinancialServiceOk(ConsumerRecords<String, Order> records) {
		
		
	}

	void processOrderFinancialServiceError(ConsumerRecords<String, Order> records) {
		
		
	}

	void processOrderFraudServiceOk(ConsumerRecords<String, Order> records) {
	
				
	}

	void processOrderFraudServiceError(ConsumerRecords<String, Order> records) {
		
	}
	

	//*private void processGroupStageFinancial(OrchestrationNewOrderEntity transactionOrder) {

		// finalizado ok: 
		//	* todos services ok
		//	* todos orquestracao un processed
		//
		
		// finalizado erro
		//  * algum  services error
		// * todos orquestracao un processed
		
		// não fazer nada
		// * qq servicoes unprocessed ou processed
	//	transactionOrder.getGroupStage(GroupStageNames.FINANCIAL).mapStages().values().stream().
		
	//}


//	private boolean isProcessedGroupStageFinancial(TransactionOrder transactionOrder) {
//		
//	}
//		return transactionOrder.dectectorFraudService.equals(StageStatus.PROCESSED_OK) 
//				&& transactionOrder.dectectorFraudOrchestration.equals(StageStatus.UNPROCESSED)
//				&& transactionOrder.financialService.equals(StageStatus.PROCESSED_OK)
//				&& transactionOrder.financialOrchestration.equals(StageStatus.UNPROCESSED);
	//}

	public void receiverOrderNew(Order order) {

		try {
			var transaction = new TransactionOrder(order.id, order);			
			//orchestrationNewOrderManager.startNewOrchestration(order.id);
			Message<Order> message = new Message<Order>(order);
			message.appendCorrelationId(OrderService.class.getSimpleName());
			producerOrder.send("ORDER_NEW", order.id, message);

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
