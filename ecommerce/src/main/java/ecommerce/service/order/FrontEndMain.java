package ecommerce.service.order;

import java.math.BigDecimal;
import java.util.Scanner;

public class FrontEndMain {

	static OrderService orderService = new OrderService();

	public static void main(String[] args) throws Exception {
		Thread.sleep(4000);
		try (Scanner teclado = new Scanner(System.in)) {
			while (true) {
				System.out.println("digite o n. do pedido");
				var id = teclado.next();
				System.out.println("digite o valor do pedido");
				var valueOrder = teclado.next();
				System.out.println("digite aidade do cliente");
				var age = teclado.nextInt();
				orderService.receiverOrderNew(new Order(id, new BigDecimal(valueOrder),age));
			}
		}
//		while(true) {
//			Thread.sleep(1000);
//		}

	}

}
