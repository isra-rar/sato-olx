package sato.olx.olx_anuncios.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sato.olx.olx_anuncios.domain.LoginResponse;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sato.olx.olx_anuncios.domain.AnuncioDTO;
import sato.olx.olx_anuncios.domain.Anuncio;
import sato.olx.olx_anuncios.repositories.AnuncioRepository;

import java.util.ArrayList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AnuncioService {
    private final AnuncioRepository anuncioRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${anuncios.size:3}")
    private int anuncioSize;

    public String customFindAll(LoginResponse loginResponse) {
        try {
            Map<String, Object> body = new HashMap<>();
            Map<String, Object> filters = new HashMap<>();
            filters.put("carregarInformacoesAoAbrirTela", true);
            body.put("filters", filters);
            body.put("sort", List.of());
            body.put("columns", List.of());
            String requestBody = objectMapper.writeValueAsString(body);
            String url = String.format("https://api.altimus.com.br/api/anuncio/custom-find-all?all=false&responseType=LIST&size=%d", anuncioSize);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("accept", "application/json")
                    .header("accept-language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("authorization", "Bearer " + loginResponse.getAccess_token())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return response.body();
            } else {
                throw new RuntimeException("Erro ao buscar anúncios: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar anúncios", e);
        }
    }

    public List<AnuncioDTO> buscarAnuncios(LoginResponse loginResponse) {
        String json = customFindAll(loginResponse);
        List<AnuncioDTO> anuncios = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode content = root.get("content");
            if (content != null && content.isArray()) {
                for (JsonNode node : content) {
                    AnuncioDTO dto = new AnuncioDTO();
                    dto.setKmSaidaEntrada(node.path("kmSaidaEntrada").asInt());
                    dto.setPlaca(node.path("placa").asText(null));
                    dto.setDescricao(node.path("descricao").asText(null));
                    dto.setValor(node.path("valor").asDouble());
                    dto.setCombustivelDescricao(node.path("combustivel").path("descricao").asText(null));
                    // Buscar status e anuncio_url dentro do objeto OLX (guid dinâmico)
                    JsonNode olxNode = null;
                    for (JsonNode child : node) {
                        if (child.has("anuncio_url")) {
                            olxNode = child;
                            break;
                        }
                    }
                    if (olxNode != null) {
                        dto.setStatus(olxNode.path("status").asText(null));
                        dto.setAnuncioUrl(olxNode.path("anuncio_url").asText(null));
                    }
                    anuncios.add(dto);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear anúncios", e);
        }
        return anuncios;
    }

    public List<Anuncio> salvarAnuncios(List<AnuncioDTO> dtos) {
        List<Anuncio> entidades = new ArrayList<>();
        for (AnuncioDTO dto : dtos) {
            if (!"PUBLICADO".equalsIgnoreCase(dto.getStatus())) {
                continue; // Só salva publicados
            }
            if (dto.getAnuncioUrl() == null) {
                continue; // Ignora se não tem URL
            }
            boolean exists = anuncioRepository.findByAnuncioUrl(dto.getAnuncioUrl()).isPresent();
            if (exists) {
                continue; // Não salva duplicado
            }
            Anuncio a = Anuncio.builder()
                .kmSaidaEntrada(dto.getKmSaidaEntrada())
                .status(dto.getStatus())
                .anuncioUrl(dto.getAnuncioUrl())
                .valor(dto.getValor())
                .placa(dto.getPlaca())
                .descricao(dto.getDescricao())
                .combustivelDescricao(dto.getCombustivelDescricao())
                .build();
            entidades.add(a);
        }
        return anuncioRepository.saveAll(entidades);
    }

    public AnuncioDTO toDTO(Anuncio a) {
        return AnuncioDTO.builder()
                .kmSaidaEntrada(a.getKmSaidaEntrada())
                .status(a.getStatus())
                .anuncioUrl(a.getAnuncioUrl())
                .valor(a.getValor())
                .placa(a.getPlaca())
                .descricao(a.getDescricao())
                .combustivelDescricao(a.getCombustivelDescricao())
                .build();
    }

    public List<Anuncio> buscarPorDescricao(String descricao) {
        return anuncioRepository.findByDescricaoIgnoreCaseContainingAndStatus(descricao, "PUBLICADO");
    }
} 