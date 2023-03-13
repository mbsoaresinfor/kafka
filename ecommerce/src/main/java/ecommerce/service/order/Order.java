package ecommerce.service.order;

import java.math.BigDecimal;

public class Order {
	
	public Order(String id, BigDecimal value, int age) {
		super();
		this.id = id;
		this.value = value;
		this.age = age;
	}
	

	@Override
	public String toString() {
		return "Order [id=" + id + ", value=" + value + ", age=" + age + "]";
	}





	public String id;
	public  BigDecimal value;
	public int age;
}
