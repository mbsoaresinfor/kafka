package ecommerce.commons.kafka;


import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import com.google.gson.Gson;


public class MessageSerializer<T> implements Serializer<T> {

	private Gson gson = new Gson();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		gson.newBuilder().registerTypeAdapter(getClass(), configs)
	}
	
	@Override
	public byte[] serialize(String topic, T data) {

		if (data == null)
			return null;
		else
			return gson.toJson(data).getBytes();

	}

}
