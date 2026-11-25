package com.example.ejerciciosProgramaticas.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titulo;

    private Integer duracion;

    private BigDecimal nota;

    private Boolean disponible;

    @ManyToOne(fetch = FetchType.LAZY)
    private Director director;

    @ManyToMany
    private Set<Genero>generos;
}
