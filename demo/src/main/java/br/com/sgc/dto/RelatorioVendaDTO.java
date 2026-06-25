package br.com.sgc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioVendaDTO {
    private Long idVenda;
    private LocalDateTime data;
    private String nomeCliente;
    private Double valorTotal;
    private String nomeVendedor;
    private List<String> produtos; // Lista do nome dos produtos e quantidades
}
