package ecommerce.fraud.detector;

public class Order {
	
	private final String id;
	private final String value;
	
	public Order(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", value=" + value + "]";
	}
	
	
	
}
