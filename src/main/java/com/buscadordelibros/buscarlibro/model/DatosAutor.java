package com.buscadordelibros.buscarlibro.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosAutor (
@JsonAlias("name") String nombre,

@JsonAlias("birth_year") int fechaDeNacimiento,

@JsonAlias("death_year") int fechaDeMuerte){
	
	public Autor toAutor() {
	    return new Autor(
	        null, 
	        this.nombre(),
	        this.fechaDeNacimiento(),
	        this.fechaDeMuerte(),
	        new ArrayList<>() // libros del autor (vacío por ahora)
	    );}
	
	

}
