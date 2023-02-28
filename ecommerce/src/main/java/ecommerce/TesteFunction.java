package ecommerce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TesteFunction {

	public TesteFunction() {
		
		var lista = new ArrayList<Integer>();
		lista.add(15);
		lista.add(19);
		lista.add(1);
		List<Integer> newLista = lista
		.stream()
		.filter(predicateMaiorIdade)
		.collect(Collectors.toList());
		System.out.println(newLista);
		
		
	}
	
	public static void main(String[] args) {

		var buildFunction = new BuildFunction();
		testeConsumer1(buildFunction::accepts);
		testeSupplier(buildFunction::get);

		new TesteFunction();
	}
	
	Predicate<Integer> predicateMaiorIdade = (s) ->  s > 15;
	
	
	
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
