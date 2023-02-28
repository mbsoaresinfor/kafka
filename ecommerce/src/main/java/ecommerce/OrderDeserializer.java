package ecommerce;


import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;

public class OrderDeserializer implements Deserializer<Order> {

	private Gson gson = new Gson();

		@Override
	public Order deserialize(String topic, byte[] data) {
		if (data == null)
			return null;
		else
			return gson.fromJson(new String(data), Order.class);
	}

}
