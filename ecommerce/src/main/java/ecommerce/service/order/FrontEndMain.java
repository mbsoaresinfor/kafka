package ecommerce.service.order;

import java.math.BigDecimal;
import java.util.Scanner;

public class FrontEndMain {

	static OrderService orderService = new OrderService();

	public static void main(String[] args) throws Exception {

		try (Scanner teclado = new Scanner(System.in)) {
			while (true) {
				System.out.println("digite o n. do pedido");
				var id = teclado.next();
				System.out.println("digite o valor do pedido");
				var valueOrder = teclado.next();
				orderService.receiverOrderNew(new Order(id, new BigDecimal(valueOrder)));
			}
		}
//		while(true) {
//			Thread.sleep(1000);
//		}

	}

}
