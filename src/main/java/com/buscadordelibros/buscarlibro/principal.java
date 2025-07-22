package com.buscadordelibros.buscarlibro;


import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buscadordelibros.buscarlibro.model.Datos;
import com.buscadordelibros.buscarlibro.model.DatosLibros;
import com.buscadordelibros.buscarlibro.service.LibroService;
import com.buscadordelibros.buscarlibro.service.consumoAPI;
import com.buscadordelibros.buscarlibro.service.convierteDatos;
@Component
public class principal {
	
	
	@Autowired
	private LibroService libroService;
	
	private static final String URL_Base = "https://gutendex.com/books/" ;
	
	private consumoAPI consumoAPI = new consumoAPI();
	
	private convierteDatos conversor = new convierteDatos();
	
	private Scanner teclado = new Scanner(System.in);
	
	
	public void mostrarMenu(){
		var json = consumoAPI.obtenerDatos(URL_Base);	
		System.out.println(json);
		var datos = conversor.obtenerDatos(json,Datos.class);
		System.out.println(datos);
		
		//top 10 de los libros mas descargados 
		
		//System.out.println("Top 10 libros mas descargados");
		//datos.Libros().stream()
		//.sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
		//.limit(10)
		//.map(l -> l.titulo().toUpperCase() )
		//.forEach(System.out::println);
		
		//Busqueda de libros por nombre 
		
		System.out.println("Ingresa el nombre del Libro");
		var busquedaLibro = teclado.nextLine();
		json = consumoAPI.obtenerDatos(URL_Base + "?search="+ busquedaLibro.replace(" ","+")); 
		var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
		Optional<DatosLibros> librobuscado = datosBusqueda.Libros().stream()
				.filter(l -> l.titulo().toUpperCase().contains(busquedaLibro.toUpperCase()))
				.findFirst();
		if (librobuscado.isPresent()) {
		    DatosLibros datosLibro = librobuscado.get();
		    System.out.println("📖 Libro encontrado:");
		    System.out.println(datosLibro);

		    libroService.guardarSiNoExiste(datosLibro);

		} else {
		    System.out.println("❌ No se encontró coincidencia.");
		}
	       
		
		//Estadisticas
		
		DoubleSummaryStatistics estadisticas = datos.Libros().stream()
				.filter(d -> d.numeroDeDescargas() >0 )
				.collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
		System.out.println("Cantidad promedio de descargas: " + estadisticas.getAverage());
		System.out.println("Cantidad maxima de descargas: " + estadisticas.getMax());
		System.out.println("Cantidad minima de descargas: " + estadisticas.getMin());
		System.out.println("Cantidad de registros de descargas: " + estadisticas.getMax());
	}
	
	
	
	
	
	

}
