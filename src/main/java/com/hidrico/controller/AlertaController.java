package com.hidrico.controller;

import com.hidrico.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaRepository alertaRepository;

    @GetMapping("/alertas")
    public String listar(Model model) {

        model.addAttribute("alertas", alertaRepository.findAll());

        return "alertas/lista";
    }
}