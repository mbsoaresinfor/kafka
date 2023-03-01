package ecommerce;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import ecommerce.commons.kafka.EmailSerializer;
import ecommerce.commons.kafka.KaftaProducerService;
import ecommerce.commons.kafka.OrderSerializer;

public class SendMessageMain {

	static KaftaProducerService<String, Order> producerOrder = new KaftaProducerService<String, Order>(
			buildPropertiesProducerOrder());

	static KaftaProducerService<String, Email> producerEmail = new KaftaProducerService<String, Email>
			(buildPropertiesProducerEmail());

	public static void main(String[] args) throws Exception {

		var tamMessage = 10;
		for (int i = 0; i < tamMessage; i++) {
			var key = UUID.randomUUID().toString();
			var value = "VALUE_" + key;
			var order = new Order(key, value);
			producerOrder.send("ECOMMERCE_NEW_ORDER", key, order);
		}

//		for (int i = 0; i < tamMessage; i++) {
//			var key = UUID.randomUUID().toString();
//			var subject = "meu titulo";
//			var body = "Mensagem de e-mail, " + key;
//			var email = new Email(subject,body);
//			producerEmail.send("ECOMMERCE_SEND_EMAIL", key, email);
//		}

	}

	private static Properties buildPropertiesProducerOrder() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
		return properties;
	}
	
	private static Properties buildPropertiesProducerEmail() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EmailSerializer.class.getName());
		return properties;
	}

}
