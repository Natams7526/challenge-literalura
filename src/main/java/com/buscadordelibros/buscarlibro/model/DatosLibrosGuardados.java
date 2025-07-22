package com.buscadordelibros.buscarlibro.model;

import java.util.List;

public record DatosLibrosGuardados (
	    String titulo,
	    Double numeroDeDescargas,
	    List<String> idiomas,
	    List<String> autores
	){

}
