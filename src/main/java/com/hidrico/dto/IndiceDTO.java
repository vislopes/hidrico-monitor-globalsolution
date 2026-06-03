package com.hidrico.dto;

import com.hidrico.model.NivelAlerta;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndiceDTO {

    private Long propriedadeId;

    private String nomePropriedade;

    private Double ndviMedio;

    private Double ndwiMedio;

    private Map<String, Double> ndviPorZona;

    private Map<String, Double> ndwiPorZona;

    private LocalDateTime dataLeitura;

    private String fonteDados;

    private NivelAlerta nivelAlerta;

    private String interpretacao;

    private String recomendacao;
}