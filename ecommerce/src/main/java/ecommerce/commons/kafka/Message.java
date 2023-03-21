package ecommerce.commons.kafka;

public class Message<T> {

	private CorrelationId correlationId;
	private T payload;
	
	public Message(T payload, String correlationId) {
		this.payload =payload;
		this.correlationId = new CorrelationId(correlationId);
	}

	public CorrelationId getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId.setCorrelationId(correlationId);
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "Message [correlationId=" + correlationId + ", payload=" + payload + "]";
	}
	
	
	
	
}
