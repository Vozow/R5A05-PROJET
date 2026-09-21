package fr.ilyas.baskip;

import org.springframework.boot.SpringApplication;

public class TestBaskipApplication {

	public static void main(String[] args) {
		SpringApplication.from(BaskipApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
