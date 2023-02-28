package ecommerce;


import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KaftaConsumerService <K,V>{

	private String topic;
	private Consumer<ConsumerRecords<K, V>> consume;
	private Properties props;	
	
	public KaftaConsumerService(String topic,Consumer<ConsumerRecords<K, V>>  consume,Properties props) {
		this.topic = topic;
		this.consume = consume;
		this.props = props;
	}
	
	public void process() {
		var consumer = new KafkaConsumer<K, V>(properties());
	    consumer.subscribe(Collections.singletonList(topic));
	    while(true) {
	        ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
	        consume.accept(records);	        	            	            
	    }
	}
    
    private Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.putAll(props);
        return properties;
    }

	
}
