package com.hidrico.repository;

import com.hidrico.model.LeituraIndice;
import com.hidrico.model.NivelAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeituraIndiceRepository extends JpaRepository<LeituraIndice, Long> {
    
    List<LeituraIndice> findByPropriedadeIdOrderByDataProcessamentoDesc(Long propriedadeId);
    
    Optional<LeituraIndice> findTopByPropriedadeIdOrderByDataProcessamentoDesc(Long propriedadeId);
    
    @Query("""
        SELECT l FROM LeituraIndice l 
        WHERE l.propriedade.id = :propriedadeId 
        AND l.dataProcessamento BETWEEN :inicio AND :fim
        ORDER BY l.dataProcessamento DESC
        """)
    List<LeituraIndice> findByPropriedadeAndPeriodo(
            @Param("propriedadeId") Long propriedadeId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
    
    @Query("""
        SELECT l FROM LeituraIndice l 
        WHERE l.nivelAlertaCalculado = :nivel 
        AND l.dataProcessamento >= :desde
        """)
    List<LeituraIndice> findLeiturasComAlerta(
            @Param("nivel") NivelAlerta nivel,
            @Param("desde") LocalDateTime desde);
}
