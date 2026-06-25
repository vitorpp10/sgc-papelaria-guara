package br.com.sgc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO {
    private Long clienteId;
    private List<ItemVendaDTO> itens;
}
