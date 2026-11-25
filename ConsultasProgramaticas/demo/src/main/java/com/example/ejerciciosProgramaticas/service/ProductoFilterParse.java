package com.example.ejerciciosProgramaticas.service;

import com.example.ejerciciosProgramaticas.DTOs.PeliculaFilterDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;

@Component
public class ProductoFilterParse {

    public PeliculaFilterDTO parsear(MultiValueMap<String,String>params){

        String titulo = params.getFirst("Titulo");

        if (titulo != null && (titulo.length() < 3 ||titulo.length()> 50)){

            throw new IllegalArgumentException("El titulo de búsqueda debe tener entre 3 y 50 caracteres");


        }

        String tituloOk = titulo;

        BigDecimal precioMin = (decimal(params.getFirst("precioMin"), "precioMin"));

        BigDecimal precioMax = (decimal(params.getFirst("precioMax"),"precioMin"));

        Integer duracion = params.getFirst("Duracion");







    }

    private BigDecimal decimal(String valor, String campo) {
        if (valor == null) return null;
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(campo + " debe ser un número decimal válido");
        }
    }

    private String Integer integer(String valor, String campo){

        re

    }



}
