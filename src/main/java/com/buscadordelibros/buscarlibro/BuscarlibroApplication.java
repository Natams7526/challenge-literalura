package com.buscadordelibros.buscarlibro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuscarlibroApplication implements CommandLineRunner {
	
	 @Autowired
	    private principal principal;

	public static void main(String[] args) {
		SpringApplication.run(BuscarlibroApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		principal.mostrarMenu();
		
	}

}
