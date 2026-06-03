package com.hidrico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alertas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    private String recomendacao;

    private String zonaAfetada;

    private Double valorIndice;

    @Enumerated(EnumType.STRING)
    private NivelAlerta nivel;

    @Enumerated(EnumType.STRING)
    private TipoAlerta tipo;

    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;
}