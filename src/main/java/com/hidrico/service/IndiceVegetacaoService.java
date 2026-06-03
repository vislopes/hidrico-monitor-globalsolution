package com.hidrico.service;

import com.hidrico.dto.IndiceDTO;
import com.hidrico.model.*;
import com.hidrico.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndiceVegetacaoService {
    
    private final LeituraIndiceRepository leituraRepository;
    private final PropriedadeRepository propriedadeRepository;
    private final AlertaRepository alertaRepository;
    private final SateliteService sateliteService;
    private final ObjectMapper objectMapper;
    
    // Limiares para classificação do NDVI
    private static final double NDVI_CRITICO = 0.2;
    private static final double NDVI_ATENCAO = 0.4;
    
    // Limiares para classificação do NDWI
    private static final double NDWI_CRITICO = -0.3;
    private static final double NDWI_ATENCAO = -0.1;
    
    /**
     * Processa novos dados de satélite para uma propriedade
     */
    @Transactional
    public IndiceDTO processarDadosSatelite(Long propriedadeId) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId)
                .orElseThrow(() -> new IllegalArgumentException("Propriedade não encontrada"));
        
        log.info("Iniciando processamento de dados de satélite para propriedade: {}", propriedade.getNome());
        
        // Busca dados do satélite
        Map<String, Object> dadosSatelite = sateliteService.buscarDadosSentinel(
                propriedade.getLatitudeCentro(),
                propriedade.getLongitudeCentro(),
                propriedade.getPoligonoGeoJson()
        );
        
        // Calcula índices
        double ndviMedio = calcularNdviMedio(dadosSatelite);
        double ndwiMedio = calcularNdwiMedio(dadosSatelite);
        
        Map<String, Double> ndviPorZona = calcularIndicesPorZona(dadosSatelite, "ndvi");
        Map<String, Double> ndwiPorZona = calcularIndicesPorZona(dadosSatelite, "ndwi");
        
        // Determina nível de alerta
        NivelAlerta nivel = determinarNivelAlerta(ndviMedio, ndwiMedio);
        
        // Salva leitura
        LeituraIndice leitura = salvarLeitura(propriedade, ndviMedio, ndwiMedio, 
                ndviPorZona, ndwiPorZona, dadosSatelite, nivel);
        
        // Gera alertas se necessário
        if (nivel != NivelAlerta.NORMAL) {
            gerarAlertas(propriedade, leitura, ndviPorZona, ndwiPorZona);
        }
        
        return construirDTO(propriedade, leitura, ndviPorZona, ndwiPorZona);
    }
    
    /**
     * Obtém a última leitura de uma propriedade
     */
    public Optional<IndiceDTO> obterUltimaLeitura(Long propriedadeId) {
        return leituraRepository.findTopByPropriedadeIdOrderByDataProcessamentoDesc(propriedadeId)
                .map(leitura -> {
                    try {
                        Map<String, Double> ndviPorZona = objectMapper.readValue(
                                leitura.getNdviPorZonaJson(), 
                                new TypeReference<Map<String, Double>>() {});
                        Map<String, Double> ndwiPorZona = objectMapper.readValue(
                                leitura.getNdwiPorZonaJson(), 
                                new TypeReference<Map<String, Double>>() {});
                        return construirDTO(leitura.getPropriedade(), leitura, ndviPorZona, ndwiPorZona);
                    } catch (Exception e) {
                        log.error("Erro ao desserializar dados da leitura", e);
                        return null;
                    }
                });
    }
    
    /**
     * Histórico de leituras de uma propriedade
     */
    public List<IndiceDTO> obterHistorico(Long propriedadeId, int dias) {
        LocalDateTime inicio = LocalDateTime.now().minusDays(dias);
        LocalDateTime fim = LocalDateTime.now();
        
        List<LeituraIndice> leituras = leituraRepository
                .findByPropriedadeAndPeriodo(propriedadeId, inicio, fim);
        
        return leituras.stream()
                .map(this::converterParaDTO)
                .toList();
    }
    
    // ========== Métodos Privados ==========
    
    private double calcularNdviMedio(Map<String, Object> dadosSatelite) {
        // NDVI = (NIR - RED) / (NIR + RED)
        // Banda NIR (B8) e RED (B4) do Sentinel-2
        
        @SuppressWarnings("unchecked")
        List<Double> bandaNir = (List<Double>) dadosSatelite.getOrDefault("B8", List.of(0.5));
        @SuppressWarnings("unchecked")
        List<Double> bandaRed = (List<Double>) dadosSatelite.getOrDefault("B4", List.of(0.3));
        
        double nirMedio = bandaNir.stream().mapToDouble(Double::doubleValue).average().orElse(0.5);
        double redMedio = bandaRed.stream().mapToDouble(Double::doubleValue).average().orElse(0.3);
        
        if (nirMedio + redMedio == 0) return 0;
        return (nirMedio - redMedio) / (nirMedio + redMedio);
    }
    
    private double calcularNdwiMedio(Map<String, Object> dadosSatelite) {
        // NDWI = (GREEN - NIR) / (GREEN + NIR)
        // Banda GREEN (B3) e NIR (B8) do Sentinel-2
        
        @SuppressWarnings("unchecked")
        List<Double> bandaGreen = (List<Double>) dadosSatelite.getOrDefault("B3", List.of(0.4));
        @SuppressWarnings("unchecked")
        List<Double> bandaNir = (List<Double>) dadosSatelite.getOrDefault("B8", List.of(0.5));
        
        double greenMedio = bandaGreen.stream().mapToDouble(Double::doubleValue).average().orElse(0.4);
        double nirMedio = bandaNir.stream().mapToDouble(Double::doubleValue).average().orElse(0.5);
        
        if (greenMedio + nirMedio == 0) return 0;
        return (greenMedio - nirMedio) / (greenMedio + nirMedio);
    }
    
    private Map<String, Double> calcularIndicesPorZona(Map<String, Object> dadosSatelite, String tipoIndice) {
        // Divide a área em quadrantes e calcula índices por zona
        Map<String, Double> resultado = new LinkedHashMap<>();
        String[] zonas = {"Norte", "Sul", "Leste", "Oeste", "Centro"};
        
        Random random = new Random(); // Simulação - substituir por cálculo real
        for (String zona : zonas) {
            double valor = tipoIndice.equals("ndvi") 
                    ? 0.3 + random.nextDouble() * 0.5  // NDVI entre 0.3 e 0.8
                    : -0.2 + random.nextDouble() * 0.4; // NDWI entre -0.2 e 0.2
            resultado.put(zona, Math.round(valor * 100.0) / 100.0);
        }
        
        return resultado;
    }
    
    private NivelAlerta determinarNivelAlerta(double ndvi, double ndwi) {
        if (ndvi < NDVI_CRITICO || ndwi < NDWI_CRITICO) {
            return NivelAlerta.CRITICO;
        } else if (ndvi < NDVI_ATENCAO || ndwi < NDWI_ATENCAO) {
            return NivelAlerta.ATENCAO;
        }
        return NivelAlerta.NORMAL;
    }
    
    private LeituraIndice salvarLeitura(Propriedade propriedade, double ndvi, double ndwi,
                                         Map<String, Double> ndviPorZona, Map<String, Double> ndwiPorZona,
                                         Map<String, Object> dadosSatelite, NivelAlerta nivel) {
        try {
            LeituraIndice leitura = LeituraIndice.builder()
                    .propriedade(propriedade)
                    .ndvi(ndvi)
                    .ndwi(ndwi)
                    .ndviPorZonaJson(objectMapper.writeValueAsString(ndviPorZona))
                    .ndwiPorZonaJson(objectMapper.writeValueAsString(ndwiPorZona))
                    .dataImagemSatelite((LocalDateTime) dadosSatelite.getOrDefault("dataImagem", LocalDateTime.now()))
                    .coberturaNuvens((Double) dadosSatelite.getOrDefault("nuvens", 10.0))
                    .fonteDados("Sentinel-2")
                    .nivelAlertaCalculado(nivel)
                    .build();
            
            return leituraRepository.save(leitura);
        } catch (Exception e) {
            log.error("Erro ao salvar leitura", e);
            throw new RuntimeException("Falha ao salvar leitura de índices", e);
        }
    }
    
    private void gerarAlertas(Propriedade propriedade, LeituraIndice leitura,
                               Map<String, Double> ndviPorZona, Map<String, Double> ndwiPorZona) {
        // Identifica zonas críticas
        for (Map.Entry<String, Double> entrada : ndwiPorZona.entrySet()) {
            String zona = entrada.getKey();
            double valorNdwi = entrada.getValue();
            double valorNdvi = ndviPorZona.getOrDefault(zona, 0.5);
            
            if (valorNdwi < NDWI_CRITICO || valorNdvi < NDVI_CRITICO) {
                Alerta alerta = Alerta.builder()
                        .propriedade(propriedade)
                        .nivel(NivelAlerta.CRITICO)
                        .tipo(TipoAlerta.ESTRESSE_HIDRICO)
                        .zonaAfetada(zona)
                        .valorIndice(valorNdwi)
                        .mensagem(String.format("Zona %s com baixa umidade no solo (NDWI: %.2f)", zona, valorNdwi))
                        .recomendacao("Recomendável reforço imediato na irrigação desta zona.")
                        .build();
                alertaRepository.save(alerta);
            } else if (valorNdwi < NDWI_ATENCAO || valorNdvi < NDVI_ATENCAO) {
                Alerta alerta = Alerta.builder()
                        .propriedade(propriedade)
                        .nivel(NivelAlerta.ATENCAO)
                        .tipo(TipoAlerta.ESTRESSE_HIDRICO)
                        .zonaAfetada(zona)
                        .valorIndice(valorNdwi)
                        .mensagem(String.format("Zona %s apresenta sinais de estresse hídrico inicial (NDWI: %.2f)", zona, valorNdwi))
                        .recomendacao("Monitorar e considerar irrigação preventiva nos próximos dias.")
                        .build();
                alertaRepository.save(alerta);
            }
        }
    }
    
    private IndiceDTO construirDTO(Propriedade propriedade, LeituraIndice leitura,
                                    Map<String, Double> ndviPorZona, Map<String, Double> ndwiPorZona) {
        return IndiceDTO.builder()
                .propriedadeId(propriedade.getId())
                .nomePropriedade(propriedade.getNome())
                .ndviMedio(leitura.getNdvi())
                .ndwiMedio(leitura.getNdwi())
                .ndviPorZona(ndviPorZona)
                .ndwiPorZona(ndwiPorZona)
                .dataLeitura(leitura.getDataProcessamento())
                .fonteDados(leitura.getFonteDados())
                .nivelAlerta(leitura.getNivelAlertaCalculado())
                .interpretacao(gerarInterpretacao(leitura))
                .recomendacao(gerarRecomendacao(leitura, ndwiPorZona))
                .build();
    }
    
    private String gerarInterpretacao(LeituraIndice leitura) {
        double ndvi = leitura.getNdvi();
        double ndwi = leitura.getNdwi();
        
        StringBuilder sb = new StringBuilder();
        
        // Interpretação NDVI
        if (ndvi >= 0.6) {
            sb.append("Vegetação saudável e densa. ");
        } else if (ndvi >= 0.4) {
            sb.append("Vegetação moderada, dentro do esperado. ");
        } else if (ndvi >= 0.2) {
            sb.append("Vegetação esparsa ou sob estresse. ");
        } else {
            sb.append("Vegetação muito degradada ou solo exposto. ");
        }
        
        // Interpretação NDWI
        if (ndwi >= 0) {
            sb.append("Bom nível de umidade no solo/vegetação.");
        } else if (ndwi >= -0.2) {
            sb.append("Umidade moderada, monitorar.");
        } else {
            sb.append("Déficit hídrico detectado.");
        }
        
        return sb.toString();
    }
    
    private String gerarRecomendacao(LeituraIndice leitura, Map<String, Double> ndwiPorZona) {
        NivelAlerta nivel = leitura.getNivelAlertaCalculado();
        
        if (nivel == NivelAlerta.CRITICO) {
            List<String> zonasCriticas = ndwiPorZona.entrySet().stream()
                    .filter(e -> e.getValue() < NDWI_CRITICO)
                    .map(Map.Entry::getKey)
                    .toList();
            
            if (!zonasCriticas.isEmpty()) {
                return String.format("AÇÃO URGENTE: Irrigar imediatamente as zonas %s. " +
                        "Verificar sistema de irrigação e disponibilidade de água.", 
                        String.join(", ", zonasCriticas));
            }
            return "AÇÃO URGENTE: Reforçar irrigação em toda a propriedade.";
        } else if (nivel == NivelAlerta.ATENCAO) {
            return "Planejar irrigação preventiva nos próximos 2-3 dias. " +
                   "Verificar previsão meteorológica.";
        }
        
        return "Manter rotina normal de manejo. Próxima análise recomendada em 5-7 dias.";
    }
    
    private IndiceDTO converterParaDTO(LeituraIndice leitura) {
        try {
            Map<String, Double> ndviPorZona = objectMapper.readValue(
                    leitura.getNdviPorZonaJson(), 
                    new TypeReference<Map<String, Double>>() {});
            Map<String, Double> ndwiPorZona = objectMapper.readValue(
                    leitura.getNdwiPorZonaJson(), 
                    new TypeReference<Map<String, Double>>() {});
            return construirDTO(leitura.getPropriedade(), leitura, ndviPorZona, ndwiPorZona);
        } catch (Exception e) {
            log.error("Erro ao converter leitura para DTO", e);
            return null;
        }
    }
}
