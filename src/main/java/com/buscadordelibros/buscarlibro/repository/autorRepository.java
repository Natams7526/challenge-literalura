package com.buscadordelibros.buscarlibro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buscadordelibros.buscarlibro.model.Autor;

public interface autorRepository extends JpaRepository<Autor, Long>{
	
	Optional<Autor> findByNombre(String nombre);

}
