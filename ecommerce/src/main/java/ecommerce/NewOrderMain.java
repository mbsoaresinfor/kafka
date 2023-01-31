package ecommerce;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		var producer = new KafkaProducer<String, String>(properties());
		
		for(int i=0; i < 10;i++) {
			var value = "chave, valor:" + new Date();
			var message = new  ProducerRecord<>("ECOMMERCE_NEW_ORDER_2",value,value);
			producer.send(message,(data,ex)-> {
				if(ex != null) {
					ex.printStackTrace();
					System.err.println("ERROR NO ENVIO DA MENSAGEM");
					return;
				}
				System.out.println("MENSAGEM ENVIADA COM SUCESSO: topic: " + data.topic() + " - partition: " + data.partition() + " - offset: " + data.offset());
			}).get();
		}

	}

	private static Properties properties() {
		var properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());		
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
}
