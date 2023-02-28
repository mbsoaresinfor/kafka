package ecommerce;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class FraudDetectorService {

	public static void main(String[] args) {

		var fraudDetectorService = new FraudDetectorService();
		var consumer = new KaftaConsumerService<String,Order>("ECOMMERCE_NEW_ORDER", fraudDetectorService::accept, properties());
		consumer.process();

	}

	void accept(ConsumerRecords<String, Order> records) {
		HelperLogKafka.log2(records, "Processing new order, checking for fraud", "Order processed");
	}


	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID().randomUUID());
		return properties;
	}
}
