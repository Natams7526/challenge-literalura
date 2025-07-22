package com.buscadordelibros.buscarlibro;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buscadordelibros.buscarlibro.model.Datos;
import com.buscadordelibros.buscarlibro.model.DatosLibros;
import com.buscadordelibros.buscarlibro.service.AutorService;
import com.buscadordelibros.buscarlibro.service.LibroService;
import com.buscadordelibros.buscarlibro.service.consumoAPI;
import com.buscadordelibros.buscarlibro.service.convierteDatos;

@Component
public class principal {

	@Autowired
	private LibroService libroService;
	
	@Autowired
	private AutorService autorService;
	

	private static final String URL_Base = "https://gutendex.com/books/";

	private consumoAPI consumoAPI = new consumoAPI();

	private convierteDatos conversor = new convierteDatos();

	private Scanner teclado = new Scanner(System.in);

	public void mostrarMenu() {
		
		 int opcion = -1;

		    while (opcion != 0) {
		        System.out.println("\n📘 MENÚ PRINCIPAL");
		        System.out.println("1. Buscar y guardar libro");
		        System.out.println("2. Listar libros guardados");
		        System.out.println("3. Top 10 libros mas decargados");
		        System.out.println("4. Listar Autores guardados");
		        System.out.println("5. Listar Autores vivos");
		        System.out.println("6. Contar libros fecha");
		        System.out.println("0. Salir");
		        System.out.print("Elige una opción: ");

		        try {
		            opcion = Integer.parseInt(teclado.nextLine());
		        } catch (NumberFormatException e) {
		            System.out.println("❌ Ingresa un número válido.");
		            continue;
		        }

		        switch (opcion) {
		            case 1:
		                buscarYGuardarLibro();
		                break;
		            case 2:
		                listarLibrosGuardados();
		                break;		                
		            case 3:
		            	listarTop10();
		                break;		                
		            case 4:
		            	listarAutores();
		                break;		                
		            case 5:
		            	listarAutoresVivos();
		                break;		                
		            case 6:
		            	listarLibrosGuardados();
		                break;
		            case 0:
		                System.out.println("👋 ¡Hasta luego!");
		                break;
		            default:
		                System.out.println("❌ Opción no válida.");
		        }
		    }

	}

	// Busqueda de libros por nombre
	private void buscarYGuardarLibro() {
		var json = consumoAPI.obtenerDatos(URL_Base);
		System.out.println(json);
		var datos = conversor.obtenerDatos(json, Datos.class);
		System.out.println(datos);
		System.out.println("Ingresa el nombre del Libro");
		var busquedaLibro = teclado.nextLine();
		json = consumoAPI.obtenerDatos(URL_Base + "?search=" + busquedaLibro.replace(" ", "+"));
		var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
		Optional<DatosLibros> librobuscado = datosBusqueda.Libros().stream()
				.filter(l -> l.titulo().toUpperCase().contains(busquedaLibro.toUpperCase())).findFirst();
		if (librobuscado.isPresent()) {
			DatosLibros datosLibro = librobuscado.get();
			System.out.println("📖 Libro encontrado:");
			System.out.println(datosLibro);

			libroService.guardarSiNoExiste(datosLibro);

		} else {
			System.out.println("❌ No se encontró coincidencia.");
		}

		// Estadisticas

		DoubleSummaryStatistics estadisticas = datos.Libros().stream().filter(d -> d.numeroDeDescargas() > 0)
				.collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
		System.out.println("Cantidad promedio de descargas: " + estadisticas.getAverage());
		System.out.println("Cantidad maxima de descargas: " + estadisticas.getMax());
		System.out.println("Cantidad minima de descargas: " + estadisticas.getMin());
		System.out.println("Cantidad de registros de descargas: " + estadisticas.getMax());
	}

	// top 10 de los libros mas descargados

	private void listarTop10() {
		var json = consumoAPI.obtenerDatos(URL_Base);
		var datos = conversor.obtenerDatos(json, Datos.class);
		System.out.println("Top 10 libros mas descargados");
		datos.Libros().stream().sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed()).limit(10)
				.map(l -> l.titulo().toUpperCase()).forEach(System.out::println);
	}
	
	// Listar los Libros Guardados 
	
	private void listarLibrosGuardados() {
	    System.out.println("📚 Libros buscados previamente:");
	    var libros = libroService.obtenerLibrosGuardados();

	    if (libros.isEmpty()) {
	        System.out.println("❌ No hay libros registrados aún.");
	        return;
	    }

	    for (int i = 0; i < libros.size(); i++) {
	        var libro = libros.get(i);
	        System.out.println("📘 Libro #" + (i + 1));
	        System.out.println("📖 Título: " + libro.titulo());
	        System.out.println("📅 Número de descargas: " + libro.numeroDeDescargas());
	        System.out.println("👨‍💼 Autores:");
	        libro.autores().forEach(autor -> System.out.println("   ✍️ " + autor));
	        //System.out.println("🧾 Idiomas: " + libro.idiomas());
	        System.out.println("------------------------");
	    }
	        
	}

	//listar autores registrados
    private void listarAutores() {
    	 System.out.println("\n👨‍💼 Todos los autores registrados:");
    	    var autores = autorService.listarTodosLosAutores();
    	    autores.forEach(autor -> System.out.println(" - " + autor.getNombre()));
    }
    
    //listar autores vivos en un año 
    private void listarAutoresVivos() {
    	 System.out.print("\n⏰ Ingresa un año para ver los autores vivos en esa fecha: ");
    	    int año = teclado.nextInt();
    	    teclado.nextLine(); // consumir salto de línea

    	    var autoresVivos = autorService.listarAutoresVivosEn(año);
    	    if (autoresVivos.isEmpty()) {
            System.out.println("❌ No hay autores vivos en ese año.");
    	    } else {
    	        System.out.println("‍💼 Autores vivos en " + año + ":");
    	        autoresVivos.forEach(autor -> System.out.println(" - " + autor.getNombre()));
    	    }
    }
    //listar lirbos por idioma
    
    private void cantidadLibrosPorIdioma() {
    	System.out.print("\n🧾 Ingresa un idioma para contar libros (ej: 'en', 'es'): ");
        String idioma = teclado.nextLine();
        long cantidad = libroService.contarLibrosPorIdioma(idioma);
        System.out.println("Hay " + cantidad + " libros en el idioma '" + idioma + "'.");
    }
}
