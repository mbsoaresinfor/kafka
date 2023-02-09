package ecommerce;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class FraudDetectorService2 {

	 public static void main(String[] args) {
	        var consumer = new KafkaConsumer<String, String>(properties());
	        consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));
	        while(true) {
	            var records = consumer.poll(Duration.ofMillis(100));	            
	            HelperLogKafka.log(records, "Processing new order, checking for fraud", "Order processed");	            	            
	        }
	        
	    }

	    private static Properties properties() {
	        var properties = new Properties();
	        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
	        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService2.class.getSimpleName());
	        return properties;
	    }
}