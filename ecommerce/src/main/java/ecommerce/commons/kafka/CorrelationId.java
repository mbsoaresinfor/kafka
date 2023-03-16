package ecommerce.commons.kafka;

import java.util.Date;
import java.util.Objects;

public class CorrelationId {

	private String correlationId;
	
	public CorrelationId(String correlationId) {
		this.setCorrelationId(correlationId);
	}
	
	public void setCorrelationId(String correlationId) {
		if(Objects.isNull(this.correlationId)) {
			this.correlationId = buildCorrelationId(correlationId);
		}else {
			correlationId = " , "  + buildCorrelationId(correlationId);
		}
	}
	
	private String buildCorrelationId(String correlationId) {
		return new StringBuilder(correlationId).append(":").append(new Date().toString()).toString();
	}
	
	public String getId() {
		return correlationId;
	}

	@Override
	public String toString() {
		return "CorrelationId [correlationId=" + correlationId + "]";
	}
	
	
	
	
}
