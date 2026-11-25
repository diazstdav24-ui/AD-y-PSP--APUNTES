package com.example.ejerciciosProgramaticas.DTOs;

import com.example.ejerciciosProgramaticas.models.Pelicula;

import java.math.BigDecimal;

public record PeliculaDTO(
        Long id,
        String titulo,
        Integer duracion,
        BigDecimal nota,
        Boolean disponible,
        String nombreDirector
){

    public static PeliculaDTO of (Pelicula p){

        return new PeliculaDTO(

                p.getId(),
                p.getTitulo(),
                p.getDuracion(),
                p.getNota(),
                p.getDisponible(),
                p.getDirector() != null ? p.getDirector().getNombre() : null

        );


    }

}


