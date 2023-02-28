package ecommerce;


import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;

public class EmailDeserializer implements Deserializer<Email> {

	private Gson gson = new Gson();

		@Override
	public Email deserialize(String topic, byte[] data) {
		if (data == null)
			return null;
		else
			return gson.fromJson(new String(data), Email.class);
	}

}
