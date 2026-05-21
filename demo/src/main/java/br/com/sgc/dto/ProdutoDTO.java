package br.com.sgc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    @PositiveOrZero(message = "Preço não pode ser negativo")
    private Double preco;

    @PositiveOrZero(message = "Quantidade em estoque não pode ser negativa")
    private Integer quantidadeEstoque;
}
