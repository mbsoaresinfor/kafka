package ecommerce.service.order;

import java.math.BigDecimal;

public class Order {
	
	public Order(String id, BigDecimal value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", value=" + value + "]";
	}


	public String id;
	public  BigDecimal value;
}
