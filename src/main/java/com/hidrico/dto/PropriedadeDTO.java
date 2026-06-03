package com.hidrico.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropriedadeDTO {

    private Long id;
    private String nome;
    private Double latitudeCentro;
    private Double longitudeCentro;
    private String nomeProdutor;
}