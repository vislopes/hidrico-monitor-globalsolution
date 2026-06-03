package com.hidrico.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapaCalorDTO {

    private Long propriedadeId;

    private List<double[]> pontosNdvi;

    private List<double[]> pontosNdwi;

    private double[] boundingBox;

    private String dataReferencia;

    private String escalaMinima;

    private String escalaMaxima;
}