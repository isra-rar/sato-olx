package sato.olx.olx_anuncios.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "anuncio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer kmSaidaEntrada;
    private String status;
    private String anuncioUrl;
    private Double valor;
    private String placa;
    private String descricao;
    private String combustivelDescricao;
} 