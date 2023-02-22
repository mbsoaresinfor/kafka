package ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KaftaConsumerService {

	private String topic;
	private Consumer consume;
	private Properties props;	
	
	public KaftaConsumerService(String topic,Consumer consume,Properties props) {
		this.topic = topic;
		this.consume = consume;
		this.props = props;
	}
	
	public void process() {
		var consumer = new KafkaConsumer<String, String>(properties());
	    consumer.subscribe(Collections.singletonList(topic));
	    while(true) {
	        var records = consumer.poll(Duration.ofMillis(100));
	        consume.consume(records);	        	            	            
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
