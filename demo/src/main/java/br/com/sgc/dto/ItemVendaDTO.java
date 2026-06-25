package br.com.sgc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Padrão DTO: Transporta dados entre as camadas evitando expor as Entidades do banco
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVendaDTO {
    private Long produtoId;
    private Integer quantidade;
}
