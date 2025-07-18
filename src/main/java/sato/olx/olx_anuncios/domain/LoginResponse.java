package sato.olx.olx_anuncios.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
} 