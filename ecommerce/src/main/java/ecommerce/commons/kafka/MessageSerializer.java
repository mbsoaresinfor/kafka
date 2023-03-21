package ecommerce.commons.kafka;


import org.apache.kafka.common.serialization.Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MessageSerializer implements Serializer<Message> {

	private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();
	
	@Override
	public byte[] serialize(String topic, Message data) {

		if (data == null)
			return null;
		else
			return gson.toJson(data).getBytes();

	}

}
