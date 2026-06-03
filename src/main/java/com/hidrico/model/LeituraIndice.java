package com.hidrico.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leituras_indice")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeituraIndice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double ndvi;

    private Double ndwi;

    @Column(columnDefinition = "TEXT")
    private String ndviPorZonaJson;

    @Column(columnDefinition = "TEXT")
    private String ndwiPorZonaJson;

    private LocalDateTime dataProcessamento;

    private LocalDateTime dataImagemSatelite;

    private Double coberturaNuvens;

    private String fonteDados;

    @Enumerated(EnumType.STRING)
    private NivelAlerta nivelAlertaCalculado;

    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;
}