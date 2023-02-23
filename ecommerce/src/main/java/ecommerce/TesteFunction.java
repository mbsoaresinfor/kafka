package ecommerce;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TesteFunction {

	public static void main(String[] args) {

		var buildFunction = new BuildFunction();
		testeConsumer1(buildFunction::accepts);
		testeSupplier(buildFunction::get);

	}
	
	public static void testeConsumer1(Consumer<String> consumer) {
		consumer.accept("marcelo soares");
	}
	
	public static void testeConsumer(Consumer<String> consumer) {
		consumer.accept("marcelo soares");
	}
	
	public static void testeSupplier(Supplier<String> supplier) {
		System.out.println(supplier.get());
	}

}
