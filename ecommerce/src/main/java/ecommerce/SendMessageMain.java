package ecommerce;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public class SendMessageMain {

	static KaftaProducerService<String, Order> producerOrder = new KaftaProducerService<String, Order>(
			buildPropertiesProduerOrder());

	static KaftaProducerService<String, String> producerEmail = new KaftaProducerService<String, String>();

	public static void main(String[] args) throws Exception {

		var tamMessage = 5;
		for (int i = 0; i < tamMessage; i++) {
			var key = UUID.randomUUID().toString();
			var value = "VALUE_" + key;
			var order = new Order(key, value);
			producerOrder.send("ECOMMERCE_NEW_ORDER", key, order);
		}

		for (int i = 0; i < tamMessage; i++) {
			var key = UUID.randomUUID().toString();
			var value = "Mensagem de e-mail, " + key;
			producerEmail.send("ECOMMERCE_SEND_EMAIL", key, value);
		}

	}

	private static Properties buildPropertiesProduerOrder() {
		var properties = new Properties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
		return properties;
	}

}
