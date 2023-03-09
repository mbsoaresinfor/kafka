package ecommerce.fraud.detector;

import java.math.BigDecimal;

public class Order {
	
	private final String id;
	private final BigDecimal value;
	
	public Order(String id, BigDecimal value) {
		super();
		this.id = id;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", value=" + value + "]";
	}
	
	
	
}
