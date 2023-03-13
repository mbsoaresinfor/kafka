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
import ecommerce.commons.kafka.OrderDeserializer;
import ecommerce.commons.kafka.OrderSerializer;
import ecommerce.financial.FinancialService;
import ecommerce.fraud.detector.FraudDetectorService;
import ecommerce.service.order.orchestration.OrchestrationNewOrderEntity;
import ecommerce.service.order.orchestration.OrchestrationNewOrderManager;
import ecommerce.service.order.orchestration.stepservice.StepGroup;
import ecommerce.service.order.orchestration.stepservice.StepGroupNames;
import ecommerce.service.order.orchestration.stepservice.StepNames;
import ecommerce.service.order.orchestration.stepservice.StepStatus;

public class OrderService {

	Map<String, TransactionOrder> mapTransactionOrder = new HashMap<String, TransactionOrder>();
	private final OrchestrationNewOrderManager orchestrationNewOrderManager = new OrchestrationNewOrderManager();

	KaftaProducerService<String, Order> producerOrder = new KaftaProducerService<String, Order>(
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

	
	private void updateStatusOfStages(ConsumerRecords<String, Order> records, 
									StepGroupNames groupStageName,StepNames stageName, StepStatus stageStatus) {
		
		
		
		for (var record : records) {
			OrchestrationNewOrderEntity orchestrationStep = mapOrchestrationNewOrder.get(record.value().id);
			if (orchestrationStep != null) {
				StepGroup groupStage =  orchestrationStep.getGroupStage(groupStageName);
				StepStatus stageStatusForUpdate = groupStage.getStages(stageName).get();
				stageStatusForUpdate =  stageStatus;				
			}
		}		
	}
	
	private List<String> listIdOrchestrationNewOrder(ConsumerRecords<String, Order> records){
		List<String> ids = new ArrayList<>();
		Iterator<ConsumerRecord<String, Order>> it = records.iterator();
		while(it.hasNext()) {
			ids.add(it.next().value().id);
		}
		return ids;
	}
	
	private void callOrchestrationOrderNew(ConsumerRecords<String, Order> records) {
		for (var record : records) {
			OrchestrationNewOrderEntity transactionOrder = mapOrchestrationNewOrder.get(record.value().id);
			if (transactionOrder != null) {
				orchestrationOrderNew(transactionOrder);
			}
		}
	}
	void processOrderFinancialServiceOk(ConsumerRecords<String, Order> records) {
		updateStatusOfStages(records,StepGroupNames.FINANCIAL,StepNames.FINANCIAL_SERVICE,StepStatus.PROCESSED_OK);
		callOrchestrationOrderNew(records);
	}

	void processOrderFinancialServiceError(ConsumerRecords<String, Order> records) {
		updateStatusOfStages(records,StepGroupNames.FINANCIAL,StepNames.FINANCIAL_SERVICE,StepStatus.PROCESSED_ERROR);
		callOrchestrationOrderNew(records);
	}

	void processOrderFraudServiceOk(ConsumerRecords<String, Order> records) {
		updateStatusOfStages(records,StepGroupNames.FINANCIAL,StepNames.DETECTOR_FRAUDE_SERVICE,StepStatus.PROCESSED_OK);
		callOrchestrationOrderNew(records);		
	}

	void processOrderFraudServiceError(ConsumerRecords<String, Order> records) {
		updateStatusOfStages(records,StepGroupNames.FINANCIAL,StepNames.DETECTOR_FRAUDE_SERVICE,StepStatus.PROCESSED_ERROR);
		callOrchestrationOrderNew(records);		
	}

	
	void orchestrationOrderNew(OrchestrationNewOrderEntity transactionOrder) {
		System.out.println("Processing ORCHESTRATIN ORDER NEW");
		
		processGroupStageFinancial(transactionOrder);

	}


	private void processGroupStageFinancial(OrchestrationNewOrderEntity transactionOrder) {

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
		
	}


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
			orchestrationNewOrderManager.startNewOrchestration(transaction);
			producerOrder.send("ORDER_NEW", order.id, order);

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
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
		properties.setProperty(OrderDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}
	
	private Properties buildPropertiesConsumerFinancial() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FinancialService.class.getSimpleName());		
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FinancialService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
		properties.setProperty(OrderDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}

}
