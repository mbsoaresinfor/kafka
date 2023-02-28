package ecommerce;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class EmailService {

	HelperLogKafka<String, Email> helperLogKafka = new HelperLogKafka<String, Email>();
	
	public static void main(String[] args) {

		var emailService = new EmailService();
		var consumer = new KaftaConsumerService<String,Email>("ECOMMERCE_SEND_EMAIL", emailService::accept, properties());
		consumer.process();
	}

	void accept(ConsumerRecords<String, Email> records) {
		helperLogKafka.log(records, "Processing emails", "email send");		
	}
	 
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());
		UUID.randomUUID();
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				EmailService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,EmailDeserializer.class.getName());
		return properties;
	}
}
