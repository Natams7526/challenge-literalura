package com.buscadordelibros.buscarlibro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buscadordelibros.buscarlibro.model.Libro;

public interface libroRepository extends JpaRepository<Libro, Long> {
	
	Optional<Libro> findByTitulo(String titulo);
	
	
	

}
