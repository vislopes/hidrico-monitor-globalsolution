package com.hidrico.controller;

import com.hidrico.dto.IndiceDTO;
import com.hidrico.model.Propriedade;
import com.hidrico.repository.PropriedadeRepository;
import com.hidrico.service.IndiceVegetacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/monitoramento")
@RequiredArgsConstructor
public class MonitoramentoController {

    private final IndiceVegetacaoService indiceVegetacaoService;
    private final PropriedadeRepository propriedadeRepository;

    @GetMapping("/{id}")
    public String dashboard(
            @PathVariable Long id,
            Model model
    ) {

        Propriedade propriedade =
                propriedadeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Propriedade não encontrada"));

        model.addAttribute("propriedade", propriedade);

        return "monitoramento/dashboard";
    }

    @PostMapping("/{id}/processar")
    @ResponseBody
    public IndiceDTO processar(@PathVariable Long id) {

        return indiceVegetacaoService
                .processarDadosSatelite(id);
    }

    @GetMapping("/{id}/ultima-leitura")
    @ResponseBody
    public IndiceDTO ultimaLeitura(@PathVariable Long id) {

        return indiceVegetacaoService
                .obterUltimaLeitura(id)
                .orElse(null);
    }
}