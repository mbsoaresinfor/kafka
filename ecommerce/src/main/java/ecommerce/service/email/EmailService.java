package ecommerce.service.email;

import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import ecommerce.commons.kafka.EmailDeserializer;
import ecommerce.commons.kafka.HelperLogKafka;
import ecommerce.commons.kafka.KaftaConsumerService;
import ecommerce.commons.kafka.KaftaProducerService;
import ecommerce.commons.kafka.Message;
import ecommerce.commons.kafka.MessageSerializer;
import ecommerce.service.order.Order;
import ecommerce.service.order.OrderError;

public class EmailService {

	HelperLogKafka<String, Message<Order>> helperLogKafka = new HelperLogKafka<String, Message<Order>>();
	static KaftaProducerService<String, Message<Order>> producerService = new KaftaProducerService<String, Message<Order>>(
			buildPropertiesProducer());
	static EmailService emailService = new EmailService();
	static KaftaConsumerService consumerOrderFinancialOk = new KaftaConsumerService<String, Message<Order>>("ECOMMERCE_ORDER_NEW_FINANCIAL_OK",
												emailService::accept, properties());
	
	public static void main(String[] args) {
		consumerOrderFinancialOk.process();
	}

	void accept(ConsumerRecords<String, Message<Order>> records) {
		helperLogKafka.log(records, "Processing emails", "email send");		
		System.out.println("Sending e-mail");
		records.forEach((o) -> {
									Order order = o.value().getPayload();
									o.value().appendCorrelationId(EmailService.class.getSimpleName());
									try {
										producerService.send("ECOMMERCE_ORDER_NEW_PROCESSED", order.id, o.value());
									} catch (Exception e) {
										e.printStackTrace();
									}
									});
		
	}
	 
	
	private static Properties buildPropertiesProducer() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
		return properties;
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());
		UUID.randomUUID();
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG,
				EmailService.class.getSimpleName() + "-" + UUID.randomUUID());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,EmailDeserializer.class.getName());
		properties.setProperty(EmailDeserializer.TYPE_CONFIG, Email.class.getName());
		return properties;
	}
}
