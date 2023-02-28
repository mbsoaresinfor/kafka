package ecommerce;


import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;

public class EmailSerializer implements Serializer<Email> {

	private Gson gson = new Gson();

	@Override
	public byte[] serialize(String topic, Email data) {

		if (data == null)
			return null;
		else
			return gson.toJson(data).getBytes();

	}

}
