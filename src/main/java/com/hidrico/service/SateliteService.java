package com.hidrico.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SateliteService {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${sentinel.hub.client-id:demo}")
    private String sentinelClientId;
    
    @Value("${sentinel.hub.client-secret:demo}")
    private String sentinelClientSecret;
    
    private static final String SENTINEL_HUB_URL = "[services.sentinel-hub.com](https://services.sentinel-hub.com)";
    
    /**
     * Busca dados do Sentinel-2 para a área especificada.
     * Em produção, faria requisições reais à API do Sentinel Hub.
     * Para o projeto acadêmico, retorna dados simulados.
     */
    public Map<String, Object> buscarDadosSentinel(Double latitude, Double longitude, String poligonoGeoJson) {
        log.info("Buscando dados Sentinel para coordenadas: {}, {}", latitude, longitude);
        
        // Em um ambiente de produção, aqui seria feita a chamada real à API:
        // 1. Autenticação OAuth2 no Sentinel Hub
        // 2. Requisição de imagens para o polígono especificado
        // 3. Download e processamento das bandas espectrais
        
        // Para fins acadêmicos, simulamos os dados retornados
        return simularDadosSentinel(latitude, longitude);
    }
    
    /**
     * Simula dados de satélite para desenvolvimento e testes.
     * Gera valores realistas baseados na localização.
     */
    private Map<String, Object> simularDadosSentinel(Double latitude, Double longitude) {
        Map<String, Object> dados = new HashMap<>();
        Random random = new Random(Double.hashCode(latitude + longitude)); // Seed baseado na localização
        
        // Simula valores de reflectância das bandas do Sentinel-2
        // B2 (Blue), B3 (Green), B4 (Red), B8 (NIR)
        int numPixels = 100; // Simula uma grade de 10x10 pixels
        
        List<Double> bandaBlue = new ArrayList<>();
        List<Double> bandaGreen = new ArrayList<>();
        List<Double> bandaRed = new ArrayList<>();
        List<Double> bandaNir = new ArrayList<>();
        
        for (int i = 0; i < numPixels; i++) {
            // Valores típicos de reflectância para vegetação saudável
            double fatorVariacao = 0.8 + random.nextDouble() * 0.4; // 0.8 a 1.2
            
            bandaBlue.add(0.05 * fatorVariacao);
            bandaGreen.add(0.08 * fatorVariacao);
            bandaRed.add(0.06 * fatorVariacao);
            bandaNir.add(0.4 * fatorVariacao); // NIR alto indica vegetação saudável
        }
        
        dados.put("B2", bandaBlue);
        dados.put("B3", bandaGreen);
        dados.put("B4", bandaRed);
        dados.put("B8", bandaNir);
        
        dados.put("dataImagem", LocalDateTime.now().minusDays(random.nextInt(5)));
        dados.put("nuvens", 5.0 + random.nextDouble() * 15.0); // 5-20% de nuvens
        dados.put("resolucao", "10m");
        dados.put("satelite", "Sentinel-2B");
        
        log.debug("Dados simulados gerados com sucesso");
        return dados;
    }
    
    /**
     * Exemplo de como seria a chamada real à API do Sentinel Hub.
     * Mantido como referência para implementação futura.
     */
    @SuppressWarnings("unused")
    private Map<String, Object> buscarDadosReais(String poligonoGeoJson) {
        // 1. Obter token de acesso
        String token = obterTokenSentinelHub();
        
        // 2. Construir requisição de imagem
        String requestBody = construirRequisicaoSentinel(poligonoGeoJson);
        
        // 3. Fazer requisição
        WebClient client = webClientBuilder
                .baseUrl(SENTINEL_HUB_URL)
                .build();
        
        // Esta seria a chamada real:
        // return client.post()
        //         .uri("/api/v1/process")
        //         .header("Authorization", "Bearer " + token)
        //         .bodyValue(requestBody)
        //         .retrieve()
        //         .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        //         .block();
        
        return Collections.emptyMap();
    }
    
    private String obterTokenSentinelHub() {
        // Implementação de autenticação OAuth2
        return "token-simulado";
    }
    
    private String construirRequisicaoSentinel(String poligonoGeoJson) {
        // Constrói o corpo da requisição no formato esperado pelo Sentinel Hub
        return String.format("""
            {
                "input": {
                    "bounds": {
                        "geometry": %s
                    },
                    "data": [{
                        "type": "S2L2A",
                        "dataFilter": {
                            "maxCloudCoverage": 30
                        }
                    }]
                },
                "output": {
                    "responses": [{
                        "identifier": "default",
                        "format": {"type": "image/tiff"}
                    }]
                }
            }
            """, poligonoGeoJson);
    }
}
