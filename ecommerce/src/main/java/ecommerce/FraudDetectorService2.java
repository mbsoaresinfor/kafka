package ecommerce;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class FraudDetectorService2 {

	HelperLogKafka<String, Order> helperLogKafka = new HelperLogKafka<String, Order>();
	
	public static void main(String[] args) {

		var fraudDetectorService2 = new FraudDetectorService2();
		var consumer = new KaftaConsumerService<String,Order>("ECOMMERCE_NEW_ORDER", 
				fraudDetectorService2::accept, properties());
		consumer.process();
	}

	void accept(ConsumerRecords<String, Order> records) {
		helperLogKafka.log(records, "Processing new order, checking for fraud", "Order processed");
	}

	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService2.class.getSimpleName());
		UUID.randomUUID();
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				FraudDetectorService2.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,OrderDeserializer.class.getName());
		return properties;
	}
}
