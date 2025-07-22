package com.buscadordelibros.buscarlibro.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.buscadordelibros.buscarlibro.model.Autor;
import com.buscadordelibros.buscarlibro.model.DatosAutor;
import com.buscadordelibros.buscarlibro.model.DatosLibros;
import com.buscadordelibros.buscarlibro.model.DatosLibrosGuardados;
import com.buscadordelibros.buscarlibro.model.Libro;
import com.buscadordelibros.buscarlibro.repository.autorRepository;
import com.buscadordelibros.buscarlibro.repository.libroRepository;




@Service
public class LibroService {
	@Autowired
    private libroRepository libroRepository;
	
	@Autowired
    private autorRepository autorRepository;   
    
    public Libro guardarSiNoExiste(DatosLibros datosLibros) {
        Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibros.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("📚 El libro ya está registrado en la base de datos.");
            return libroExistente.get();
        }

        // Convertir DTO a entidad
        Libro libro = datosLibros.toLibro();

        // Verificar si los autores ya existen
        List<Autor> autoresVerificados = datosLibros.autor().stream()
                .map(datosAutor -> autorYaExiste(datosAutor).orElseGet(() -> datosAutor.toAutor()))
                .collect(Collectors.toList());

        libro.setAutores(autoresVerificados);

        Libro guardado = libroRepository.save(libro);
        System.out.println("✅ Libro y autores guardados exitosamente.");
        return guardado;
    }

    private Optional<Autor> autorYaExiste(DatosAutor datosAutor) {
        return autorRepository.findByNombre(datosAutor.nombre());
    }
    
    @Transactional(readOnly = true)
    public List<DatosLibrosGuardados> obtenerLibrosGuardados() {
        var libros = libroRepository.findAll(); 
        return libros.stream()
            .map(libro -> new DatosLibrosGuardados(
                libro.getTitulo(),
                libro.getNumeroDeDescargas(),
                libro.getIdiomas(),
                libro.getAutores().stream()
                      .map(Autor::getNombre)
                      .toList()
            ))
            .toList();
    }
    
    public long contarLibrosPorIdioma(String idioma) {
        return libroRepository.countByIdiomasContaining(idioma);
    }
}
