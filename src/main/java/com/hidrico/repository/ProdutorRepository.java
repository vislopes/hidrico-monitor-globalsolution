package com.hidrico.repository;

import com.hidrico.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
    Optional<Produtor> findByEmail(String email);
    Optional<Produtor> findByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
