package sato.olx.olx_anuncios.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnuncioDTO {
    private Integer kmSaidaEntrada;
    private String status;
    private String anuncioUrl;
    private Double valor;
    private String placa;
    private String descricao;
    private String combustivelDescricao;
} 