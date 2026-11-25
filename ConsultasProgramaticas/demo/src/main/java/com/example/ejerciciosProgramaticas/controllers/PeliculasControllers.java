package com.example.ejerciciosProgramaticas.controllers;

import com.example.ejerciciosProgramaticas.DTOs.PeliculaFilterDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RequiredArgsConstructor
@RestController
@Validated
public class PeliculasControllers {


    @GetMapping("/api/v1/peliculas/search-a")
    public PeliculaFilterDTO filtrarPorTodo(

            @RequestParam(required = false) @Size(min = 3, max = 100) String titulo,
            @RequestParam(required = false) @DecimalMin("0.0") Double notaMin,
            @RequestParam(required = false) @DecimalMax("0.0") Double notaMax,
            @RequestParam(required = false) String nombreDirector,
            @PageableDefault(size = 5)Pageable pageable

    ){





    }



    @GetMapping("/api/v1/peliculas/search-b")
    public PeliculaFilterDTO filtrarPorTodoMultiValue(




    ){





    }


}
