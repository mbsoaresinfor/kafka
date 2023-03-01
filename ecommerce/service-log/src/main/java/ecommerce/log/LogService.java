package ecommerce.log;

import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import ecommerce.commons.kafka.HelperLogKafka;
import ecommerce.commons.kafka.KaftaConsumerService;

public class LogService {

	HelperLogKafka<String, String> helperLogKafka = new HelperLogKafka<String, String>();

	public static void main(String[] args) {

		var logService = new LogService();
		var consumer = new KaftaConsumerService<String, String>(Pattern.compile("ECOMMERCE.*"), logService::accept,
				properties());
		consumer.process();

	}

	void accept(ConsumerRecords<String, String> records) {
		helperLogKafka.log(records, "Processing new log", "log");
	}

	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());
		UUID.randomUUID();
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				LogService.class.getSimpleName() + "-" + UUID.randomUUID());
		return properties;
	}
}
