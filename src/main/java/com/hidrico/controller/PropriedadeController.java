package com.hidrico.controller;

import com.hidrico.dto.PropriedadeDTO;
import com.hidrico.model.Propriedade;
import com.hidrico.model.Produtor;
import com.hidrico.repository.PropriedadeRepository;
import com.hidrico.repository.ProdutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/propriedades")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeRepository propriedadeRepository;
    private final ProdutorRepository produtorRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("propriedades", propriedadeRepository.findAll());
        return "propriedades/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("propriedade", new PropriedadeDTO());
        return "propriedades/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute PropriedadeDTO dto) {

        Produtor produtor = produtorRepository.findById(1L).orElse(null);

        Propriedade propriedade = Propriedade.builder()
                .nome(dto.getNome())
                .latitudeCentro(dto.getLatitudeCentro())
                .longitudeCentro(dto.getLongitudeCentro())
                .produtor(produtor)
                .build();

        propriedadeRepository.save(propriedade);

        return "redirect:/propriedades";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model) {

        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Propriedade não encontrada"));

        model.addAttribute("propriedade", propriedade);

        return "propriedades/detalhes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        propriedadeRepository.deleteById(id);

        return "redirect:/propriedades";
    }
}
