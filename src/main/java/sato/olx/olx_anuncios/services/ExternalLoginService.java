package sato.olx.olx_anuncios.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sato.olx.olx_anuncios.domain.LoginRequest;
import sato.olx.olx_anuncios.domain.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalLoginService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(loginRequest);
            String url = "https://api.altimus.com.br/api/login/externo";
            log.info("Enviando requisição de login externo para URL: {}", url);
            log.info("Corpo da requisição: {}", requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("accept", "application/json")
                    .header("accept-language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Status da resposta: {}", response.statusCode());
            log.info("Corpo da resposta: {}", response.body());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), LoginResponse.class);
            } else {
                log.error("Erro ao fazer login externo: {}", response.body());
                throw new RuntimeException("Erro ao fazer login externo: " + response.body());
            }
        } catch (Exception e) {
            log.error("Exceção ao fazer login externo", e);
            throw new RuntimeException("Erro ao fazer login externo", e);
        }
    }
} 