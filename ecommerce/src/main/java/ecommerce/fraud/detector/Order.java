package ecommerce.fraud.detector;

import java.math.BigDecimal;

public class Order {
	
	private final String id;
	private final BigDecimal value;
	private final int age;
	
	

	
	public Order(String id, BigDecimal value,int age) {
		super();
		this.id = id;
		this.value = value;
		this.age = age;
	}




	@Override
	public String toString() {
		return "Order [id=" + id + ", value=" + value + ", age=" + age + "]";
	}

	
	
	
	
}
