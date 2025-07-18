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

@Service
@RequiredArgsConstructor
public class ExternalLoginService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(loginRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.altimus.com.br/api/login/externo"))
                    .header("accept", "application/json")
                    .header("accept-language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), LoginResponse.class);
            } else {
                throw new RuntimeException("Erro ao fazer login externo: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer login externo", e);
        }
    }
} 