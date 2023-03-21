package ecommerce.commons.kafka;


import org.apache.kafka.common.serialization.Deserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageDeserializer implements Deserializer<Message> {

	public static final String TYPE_CONFIG = "type_config";
	
	private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();
		
	@Override
	public Message deserialize(String topic, byte[] data) {		
		return gson.fromJson(new String(data), Message.class);
	}

}
