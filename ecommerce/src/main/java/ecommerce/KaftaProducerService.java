package ecommerce;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KaftaProducerService<K, V>{

	private KafkaProducer<K,V> producer;
	
	public KaftaProducerService() {
		this(new Properties());
	}
	
	public KaftaProducerService(Properties prop) {
		Properties properties = properties();
		properties.putAll(prop);
		producer = new KafkaProducer<K,V>(properties);		
	}
	

	public void send(String topic, K key, V value) throws Exception {
		var message = new ProducerRecord<>(topic, key, value);
		producer.send(message, callBack()).get();
	}

	private static Callback callBack() {
		return (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				System.err.println("ERROR NO ENVIO DA MENSAGEM");
				return;
			}
			System.out.println("MENSAGEM ENVIADA COM SUCESSO: topic: " + data.topic() + " - partition: "
					+ data.partition() + " - offset: " + data.offset());
		};
	}

	private Properties properties() {
		var properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
}
