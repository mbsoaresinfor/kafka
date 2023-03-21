package ecommerce.commons.kafka;

import java.util.Date;
import java.util.Objects;

public class CorrelationId {

	private String correlationId;
	
	public CorrelationId(String correlationId) {
		this.correlationId =  buildCorrelationId(correlationId);
	}
	
	public void appendCorrelationId(String correlationId) {
		if(isInvalidCorrelationId(correlationId)) return;
		this.correlationId = new StringBuilder(this.correlationId)
							.append(" | ")
							.append(buildCorrelationId(correlationId))							
							.toString();
	}
	
	private boolean isInvalidCorrelationId(String correlationId) {
		return correlationId == null || "".equals(correlationId);
	}
	
	private String buildCorrelationId(String correlationId) {
		if(isInvalidCorrelationId(correlationId)) return "";
		else {
			return new StringBuilder(correlationId)
				.append(":")
				.append("teste")				
				.toString();
		}				
	}
	
	public String getCorrelationId() {
		return correlationId;
	}

	@Override
	public String toString() {
		return "CorrelationId [" + correlationId + "]";
	}
	
	
	
	
}
