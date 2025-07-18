package sato.olx.olx_anuncios.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sato.olx.olx_anuncios.domain.LoginRequest;
import sato.olx.olx_anuncios.services.ExternalLoginService;
import sato.olx.olx_anuncios.services.AnuncioService;
import sato.olx.olx_anuncios.domain.AnuncioDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sato.olx.olx_anuncios.domain.Anuncio;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/anuncios")
@Tag(name = "Gerenciamento de Calendário Google", description = "Endpoints para marcar, editar e cancelar eventos no Google Calendar.")
public class ExternalLoginController {


    private final ExternalLoginService externalLoginService;

    private final AnuncioService anuncioService;

    @PostMapping
    public ResponseEntity<List<AnuncioDTO>> getAndSaveAnuncios(@RequestBody LoginRequest loginRequest) {
        var loginResponse = externalLoginService.login(loginRequest);
        var anuncios = anuncioService.buscarAnuncios(loginResponse);
        var salvos = anuncioService.salvarAnuncios(anuncios);
        var dtos = salvos.stream().map(anuncioService::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Anuncio>> searchByDescricao(@RequestParam("description") String query) {
        List<Anuncio> anuncios = anuncioService.buscarPorDescricao(query);
        return ResponseEntity.ok(anuncios);
    }
} 