package ecommerce;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class FraudDetectorService2 {

	public static void main(String[] args) {

		var fraudDetectorService2 = new FraudDetectorService2();
		var consumer = new KaftaConsumerService("ECOMMERCE_NEW_ORDER", fraudDetectorService2::accept, properties());
		consumer.process();
	}

	void accept(ConsumerRecords<String, String> records) {
		HelperLogKafka.log(records, "Processing new order, checking for fraud", "Order processed");
	}

	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService2.class.getSimpleName());
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService2.class.getSimpleName() + "-" + UUID.randomUUID().randomUUID());
		return properties;
	}
}
