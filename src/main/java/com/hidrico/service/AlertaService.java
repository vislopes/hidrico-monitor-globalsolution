package com.hidrico.service;

import com.hidrico.model.Alerta;
import com.hidrico.model.NivelAlerta;
import com.hidrico.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;

    /**
     * Lista todos os alertas
     */
    public List<Alerta> listarTodos() {
        return alertaRepository.findAll();
    }

    /**
     * Busca alerta por ID
     */
    public Alerta buscarPorId(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta não encontrado"));
    }

    /**
     * Salva alerta
     */
    public Alerta salvar(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    /**
     * Remove alerta
     */
    public void deletar(Long id) {
        alertaRepository.deleteById(id);
    }

    /**
     * Lista alertas críticos
     */
    public List<Alerta> listarCriticos() {
        return alertaRepository.findAll()
                .stream()
                .filter(a -> a.getNivel() == NivelAlerta.CRITICO)
                .toList();
    }
}