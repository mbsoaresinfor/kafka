package ecommerce;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		var producer = new KafkaProducer<String, String>(properties());
				
		var value = "chave, valor:" + new Date();
		var message = new  ProducerRecord<>("ECOMMERCE_NEW_ORDER",value,value);
		producer.send(message,callBack()).get();
		
		var value2 = "bem vindo, " + new Date();
		var message2 = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", value2,value2);
		producer.send(message2,callBack()).get();
		
		

	}

	private static Callback callBack() {
		return (data,ex)-> {
			if(ex != null) {
				ex.printStackTrace();
				System.err.println("ERROR NO ENVIO DA MENSAGEM");
				return;
			}
			System.out.println("MENSAGEM ENVIADA COM SUCESSO: topic: " + data.topic() + " - partition: " + data.partition() + " - offset: " + data.offset());
		};
	}

	private static Properties properties() {
		var properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());		
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
}
