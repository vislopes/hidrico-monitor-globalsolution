package com.hidrico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "propriedades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Double latitudeCentro;

    private Double longitudeCentro;

    @Column(columnDefinition = "TEXT")
    private String poligonoGeoJson;

    // RELACIONAMENTO COM PRODUTOR
    @ManyToOne
    @JoinColumn(name = "produtor_id")
    private Produtor produtor;
}