package com.buscadordelibros.buscarlibro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.buscadordelibros.buscarlibro.model.Autor;
import com.buscadordelibros.buscarlibro.repository.autorRepository;

@Service
public class AutorService {
	private final autorRepository autorRepository;

    public AutorService(autorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEn(int fecha) {
        return autorRepository.findByFechaDeNacimientoBeforeAndFechaDeMuerteAfter(fecha, fecha);
    }
	
}
