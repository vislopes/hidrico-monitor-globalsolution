package com.hidrico.repository;

import com.hidrico.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {
    
    List<Propriedade> findByProdutorId(Long produtorId);
    
    @Query("SELECT p FROM Propriedade p WHERE p.produtor.id = :produtorId AND p.id = :propriedadeId")
    Propriedade findByProdutorAndId(@Param("produtorId") Long produtorId, 
                                     @Param("propriedadeId") Long propriedadeId);
    
    // Busca propriedades dentro de um raio (para funcionalidades futuras)
    @Query(value = """
        SELECT * FROM propriedades 
        WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(latitude_centro)) 
        * cos(radians(longitude_centro) - radians(:lon)) 
        + sin(radians(:lat)) * sin(radians(latitude_centro)))) <= :raioKm
        """, nativeQuery = true)
    List<Propriedade> findPropriedadesNoRaio(@Param("lat") Double latitude, 
                                              @Param("lon") Double longitude, 
                                              @Param("raioKm") Double raioKm);
}
