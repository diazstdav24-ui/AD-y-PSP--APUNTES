package com.example.ejerciciosProgramaticas.DTOs;

import java.math.BigDecimal;


public record PeliculaFilterDTO(

    String titulo,

    BigDecimal notaMedia,
    BigDecimal notaMin,
    BigDecimal notaMax,
    BigDecimal duracionMax,

    String nombreDirector,

    Boolean disponible
){


}
