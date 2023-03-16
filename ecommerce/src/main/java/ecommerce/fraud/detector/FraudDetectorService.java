package ecommerce.fraud.detector;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import ecommerce.commons.kafka.HelperLogKafka;
import ecommerce.commons.kafka.KaftaConsumerService;
import ecommerce.commons.kafka.KaftaProducerService;
import ecommerce.commons.kafka.Message;
import ecommerce.commons.kafka.MessageDeserializer;
import ecommerce.commons.kafka.MessageSerializer;
import ecommerce.service.order.Order;

public class FraudDetectorService {

	HelperLogKafka<String, Message<Order>> helperLogKafka = new HelperLogKafka<String, Message<Order>>();
	static KaftaProducerService<String, Message<Order>> producerService = new KaftaProducerService<String, Message<Order>>(
			buildPropertiesProducer());

	public static void main(String[] args) throws Exception {

		var fraudDetectorService = new FraudDetectorService();

		var consumerOrderNew = new KaftaConsumerService<String, Message<Order>>("ORDER_NEW",
				fraudDetectorService::processOrderNew, buildPropertiesConsumer());
		consumerOrderNew.process();

		var consumerOrderCancel = new KaftaConsumerService<String, Message<Order>>("ORDER_CANCEL", 
				fraudDetectorService::processOrderCancel,
			buildPropertiesConsumer());
		consumerOrderCancel.process();

	}

	void processOrderCancel(ConsumerRecords<String, Message<Order>> records) {
		// IMPLLEENTAR SAGA
		helperLogKafka.log(records, "Processing cancel order", "Order processed");

	}

	void processOrderNew(ConsumerRecords<String, Message<Order>> records) {
		try {
			helperLogKafka.log(records, "Processing new order, checking for fraud", "Order processed");
			for (var record : records) {
				Message<Order> message = record.value();
				var order = record.value().getPayload();
				if (order.value.floatValue() > 1000) {
					System.out.println("The order " + order.id + " is one fraude");
					message.setCorrelationId(FraudDetectorService.class.getSimpleName());
					producerService.send("ORDER_FRAUD_ERROR", order.id, message);
				} else {
					System.out.println("The order " + order.id + " is OK");
					message.setCorrelationId(FraudDetectorService.class.getSimpleName());
					producerService.send("ORDER_FRAUD_OK", order.id, message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Properties buildPropertiesProducer() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
		return properties;
	}

	private static Properties buildPropertiesConsumer() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());		
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
		properties.setProperty(MessageDeserializer.TYPE_CONFIG, Message.class.getName());
		return properties;
	}
}
