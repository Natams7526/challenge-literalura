package com.buscadordelibros.buscarlibro.service;

public interface IconvierteDatos {
	<T> T obtenerDatos(String json, Class<T> clase);
}
