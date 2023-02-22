package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface Consumer {

	void consume(ConsumerRecords<String, String> recordes);	
	
}
