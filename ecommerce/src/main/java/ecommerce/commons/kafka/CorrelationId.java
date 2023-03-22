package ecommerce.commons.kafka;

import java.util.Date;
import java.util.Objects;

public class CorrelationId {

	private String correlationId = "";
	
	public CorrelationId() {		
	}
	
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	public String getCorrelationId() {
		return correlationId;
	}

	public void appendCorrelationId(String correlationId) {
				
		Objects.requireNonNull(correlationId);		
		
		var stringBuilder = new StringBuilder();
		
		if(this.correlationId.equals("") == false){
			stringBuilder
			.append(this.correlationId)				
			.append(",");
		}		
		
		this.correlationId = stringBuilder
							.append(buildCorrelationId(correlationId))							
							.toString();		
	}	
	
	private String buildCorrelationId(String correlationId) {		
			return new StringBuilder(correlationId)
				.append(":")
				.append(new Date())				
				.toString();						
	}
	
	
	@Override
	public String toString() {
		return "CorrelationId [" + correlationId + "]";
	}
	
	
	
	
}
