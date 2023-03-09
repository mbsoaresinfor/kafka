package ecommerce.service.order;

public class TransactionOrder {

	Order order;
	String id;
	StageStatus dectectorFraud;
	
	public TransactionOrder(Order order) {
		this.order = order;
	}

	public TransactionOrder(String id, Order order) {
		this(order);
		this.id = id;
	}
	
}
