package ecommerce;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class EmailService {

	public static void main(String[] args) {

		var fraudDetectorService = new FraudDetectorService();
		var consumer = new KaftaConsumerService("ECOMMERCE_SEND_EMAIL", fraudDetectorService::accept, properties());
		consumer.process();
	}

	void accept(ConsumerRecords<String, String> records) {
		HelperLogKafka.log(records, "Processing emails", "email send");
	}

	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				EmailService.class.getSimpleName() + "-" + UUID.randomUUID().randomUUID());
		return properties;
	}
}
