package br.com.sgc.service;

import br.com.sgc.domain.model.Produto;
import br.com.sgc.domain.repository.ProdutoRepository;
import br.com.sgc.dto.ProdutoDTO;
import br.com.sgc.exception.BusinessException;
import br.com.sgc.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> obterTodos() {
        return produtoRepository.findAll();
    }

    public Produto obterPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    public Produto salvar(ProdutoDTO produtoDTO) {
        // Valida se o preço não é negativo
        if (produtoDTO.getPreco() < 0) {
            throw new BusinessException("Preço não pode ser negativo");
        }

        Produto produto = new Produto();
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidadeEstoque(produtoDTO.getQuantidadeEstoque());

        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, ProdutoDTO produtoDTO) {
        Produto produto = obterPorId(id);

        if (produtoDTO.getPreco() < 0) {
            throw new BusinessException("Preço não pode ser negativo");
        }

        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidadeEstoque(produtoDTO.getQuantidadeEstoque());

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        obterPorId(id);
        produtoRepository.deleteById(id);
    }
}
