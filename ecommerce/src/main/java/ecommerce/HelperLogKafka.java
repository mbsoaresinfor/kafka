package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public class HelperLogKafka {

	public static void log(ConsumerRecords<String,String> records, String message1,String message2) {
		 if (!records.isEmpty()) {
			 System.out.println("#######################################################");
			 System.out.println("Encontrei " + records.count() + " registros");
			 int ind=1;
             for (var record : records) {
            	 System.out.println("------------------------------------------");
            	 System.out.println("recods " + (ind++));
                 System.out.println(message1);
                 System.out.println("key: " + record.key());
                 System.out.println("value: " + record.value());
                 System.out.println("partition: " + record.partition());
                 System.out.println("topic: "+ record.topic());
                 System.out.println("offset: "+ record.offset());
                 try {
                     Thread.sleep(5000);
                 } catch (InterruptedException e) {
                     // ignoring
                     e.printStackTrace();
                 }
                 System.out.println(message2);
                 
             }
             System.out.println("#######################################################\n");
         }
	}
}
