package ecommerce.commons.kafka;


import org.apache.kafka.common.serialization.Serializer;
import com.google.gson.Gson;


public class OrderSerializer<T> implements Serializer<T> {

	private Gson gson = new Gson();

	@Override
	public byte[] serialize(String topic, T data) {

		if (data == null)
			return null;
		else
			return gson.toJson(data).getBytes();

	}

}
