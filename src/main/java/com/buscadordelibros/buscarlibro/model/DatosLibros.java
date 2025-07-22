package com.buscadordelibros.buscarlibro.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosLibros(
		
@JsonAlias("title") String titulo,

@JsonAlias("authors") List<DatosAutor> autor,

@JsonAlias("languages") List<String> idiomas,

@JsonAlias("download_count") double numeroDeDescargas) {
	
	public Libro toLibro() {
	    return new Libro(this.titulo(), this.idiomas(), this.numeroDeDescargas());
	}

}
