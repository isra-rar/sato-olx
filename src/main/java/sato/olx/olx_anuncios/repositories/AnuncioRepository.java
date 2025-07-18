package sato.olx.olx_anuncios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sato.olx.olx_anuncios.domain.Anuncio;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {
    List<Anuncio> findByDescricaoIgnoreCaseContaining(String descricao);
    Optional<Anuncio> findByAnuncioUrl(String anuncioUrl);
    List<Anuncio> findByDescricaoIgnoreCaseContainingAndStatus(String descricao, String status);
} 