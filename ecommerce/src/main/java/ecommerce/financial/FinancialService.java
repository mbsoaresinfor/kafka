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
import ecommerce.commons.kafka.OrderDeserializer;
import ecommerce.commons.kafka.OrderSerializer;
import ecommerce.service.order.Order;

public class FinancialService {

	HelperLogKafka<String, Order> helperLogKafka = new HelperLogKafka<String, Order>();
	static KaftaProducerService<String, Order> producerService = new KaftaProducerService<String, Order>(
			buildPropertiesProducer());

	public static void main(String[] args) throws Exception {

		var financialService = new FinancialService();

		var consumerOrderNew = new KaftaConsumerService<String, Order>("ORDER_NEW",
				financialService::processOrderNew, buildPropertiesConsumer());
		consumerOrderNew.process();

		var consumerOrderCancel = new KaftaConsumerService<String, Order>("ORDER_CANCEL", 
				financialService::processOrderCancel,
			buildPropertiesConsumer());
		consumerOrderCancel.process();

	}

	void processOrderCancel(ConsumerRecords<String, Order> records) {
		// IMPLLEENTAR SAGA
		helperLogKafka.log(records, "Processing cancel order", "Order processed");

	}

	void processOrderNew(ConsumerRecords<String, Order> records) {
		try {
			helperLogKafka.log(records, "Processing new order, checking financial", "Order processed");
			for (var record : records) {
				var order = record.value();
				if (order.age < 18 ) {
					System.out.println("The order " + order.id + " has problem financial");
					producerService.send("ORDER_FINANCIAL_ERROR", order.id, order);
				} else {
					System.out.println("The order " + order.id + " is OK financial");
					producerService.send("ORDER_FINANCIAL_OK", order.id, order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Properties buildPropertiesProducer() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
		return properties;
	}

	private static Properties buildPropertiesConsumer() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FinancialService.class.getSimpleName());		
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FinancialService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderDeserializer.class.getName());
		properties.setProperty(OrderDeserializer.TYPE_CONFIG, Order.class.getName());
		return properties;
	}
}
