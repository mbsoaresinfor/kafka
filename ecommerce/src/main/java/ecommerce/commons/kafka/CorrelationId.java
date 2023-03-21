package ecommerce.commons.kafka;

import java.util.Date;
import java.util.Objects;

public class CorrelationId {

	private String correlationId;
	
	public CorrelationId(String correlationId) {
		this.setCorrelationId(correlationId);
	}
	
	public void setCorrelationId(String correlationId) {
		buildCorrelationId(correlationId);
	}
	
	private String buildCorrelationId(String correlationId) {
		return new StringBuilder(this.correlationId == null ? "" : this.correlationId)
				.append("\n")
				.append(correlationId == null ? "" : correlationId)
				.append(":")
				.append(new Date().toString()).toString();
	}
	
	public String getCorrelationId() {
		return correlationId;
	}

	@Override
	public String toString() {
		return "CorrelationId [correlationId=" + correlationId + "]";
	}
	
	
	
	
}
