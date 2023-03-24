package ecommerce.financial;

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

public class FinancialService {

	HelperLogKafka<String, Message<Order>> helperLogKafka = new HelperLogKafka<String, Message<Order>>();
	static KaftaProducerService<String, Message<Order>> producerService = new KaftaProducerService<String, Message<Order>>(
			buildPropertiesProducer());

	public static void main(String[] args) throws Exception {

		var financialService = new FinancialService();

		var consumerOrderFraudOK = new KaftaConsumerService<String, Message<Order>>("ECOMMERCE_ORDER_NEW_FRAUD_OK",
				financialService::processOrderNewFraudOk, buildPropertiesConsumer());
		consumerOrderFraudOK.process();

		var consumerOrderNewError = new KaftaConsumerService<String, Message<Order>>("ECOMMERCE_ORDER_NEW_ERROR", 
				financialService::processOrderNewError,
			buildPropertiesConsumer());
		consumerOrderNewError.process();

	}

	void processOrderNewError(ConsumerRecords<String, Message<Order>> records) {
		// IMPLLEENTAR SAGA
		helperLogKafka.log(records, "Processing cancel order", "Order processed");

	}

	void processOrderNewFraudOk(ConsumerRecords<String, Message<Order>> records) {
		try {
			helperLogKafka.log(records, "Processing order fraud ok, checking financial", "Order processed");
			for (var record : records) {
				Message<Order> message = record.value();
				var order = message.getPayload();
				if (order.age < 18 ) {
					System.out.println("The order " + order.id + " has problem financial");
					message.appendCorrelationId(FinancialService.class.getSimpleName());
					producerService.send("ECOMMERCE_ORDER_NEW_ERROR", order.id, record.value());
				} else {
					System.out.println("The order " + order.id + " is OK financial");
					message.appendCorrelationId(FinancialService.class.getSimpleName());
					producerService.send("ECOMMERCE_ORDER_NEW_FINANCIAL_OK", order.id, record.value());
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
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FinancialService.class.getSimpleName());		
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FinancialService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
		properties.setProperty(MessageDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}
}
