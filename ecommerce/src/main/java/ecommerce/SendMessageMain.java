package ecommerce;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;


public class SendMessageMain {

	static KaftaProducerService<String,Order> producerOrder = 
			new KaftaProducerService<String,Order>(buildPropertiesProduerOrder());
	
	public static void main(String[] args) throws Exception {

		
		for (int i = 0; i < 5; i++) {
			var key = UUID.randomUUID().toString();
			var value = "VALUE_" + key;
			var order = new Order(key,value);
			producerOrder.send("ECOMMERCE_NEW_ORDER", key, order);
		}
//		var producer = new KafkaProducer<String, String>(properties());
//		for (int i = 0; i < 5; i++) {
//			var value = "chave, valor:" + UUID.randomUUID().toString();
//			var message = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);
//			producer.send(message, callBack()).get();
//
//			var value2 = "bem vindo, " + UUID.randomUUID().toString();
//			var message2 = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", value2, value2);
//			producer.send(message2, callBack()).get();
//		}

	}
	
	private static Properties buildPropertiesProduerOrder() {
		var properties = new Properties();		
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
		return properties;		
	}

	}
