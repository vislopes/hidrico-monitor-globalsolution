package com.hidrico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "zonas_monitoramento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonaMonitoramento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeZona;

    private Double latitude;

    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;
}
