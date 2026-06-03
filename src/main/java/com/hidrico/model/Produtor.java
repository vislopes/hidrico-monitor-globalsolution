package com.hidrico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produtor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;
    
    @Email(message = "Email inválido")
    @Column(unique = true)
    private String email;
    
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @Column(unique = true)
    private String cpf;
    
    private String telefone;
    
    @OneToMany(mappedBy = "produtor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Propriedade> propriedades = new ArrayList<>();
    
    @Column(name = "data_cadastro")
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    private Boolean ativo = true;
}

