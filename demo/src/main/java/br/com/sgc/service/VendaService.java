package br.com.sgc.service;

import br.com.sgc.domain.model.Cliente;
import br.com.sgc.domain.model.ItemVenda;
import br.com.sgc.domain.model.Produto;
import br.com.sgc.domain.model.Usuario;
import br.com.sgc.domain.model.Venda;
import br.com.sgc.domain.repository.ClienteRepository;
import br.com.sgc.domain.repository.ProdutoRepository;
import br.com.sgc.domain.repository.UsuarioRepository;
import br.com.sgc.domain.repository.VendaRepository;
import br.com.sgc.dto.ItemVendaDTO;
import br.com.sgc.dto.RelatorioVendaDTO;
import br.com.sgc.dto.VendaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // A anotação Transactional garante a atomicidade: ou a venda e o estoque são salvos juntos, ou nada é feito.
    @Transactional
    public Venda registrarVenda(VendaDTO dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível realizar uma venda sem itens.");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        // Obtém o usuário logado via Contexto de Segurança (Token JWT)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setUsuario(usuario);
        venda.setData(LocalDateTime.now());

        double valorTotal = 0.0;
        List<ItemVenda> itensVenda = new ArrayList<>();

        for (ItemVendaDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            // Regra de Negócio: Controle de Estoque
            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            // Atualiza o estoque do produto após confirmar que há quantidade suficiente
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto); // Salva a baixa no estoque

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setVenda(venda);

            // Regra de Negócio: Valor total calculado automaticamente
            valorTotal += (produto.getPreco() * itemDto.getQuantidade());

            itensVenda.add(item);
        }

        venda.setItens(itensVenda);
        venda.setValorTotal(valorTotal);

        return vendaRepository.save(venda);
    }

    // Geração do relatório com DTO para proteger a entidade real e enviar apenas o necessário
    public List<RelatorioVendaDTO> gerarRelatorio(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = vendaRepository.findByDataBetween(inicio, fim);
        
        return vendas.stream().map(v -> {
            List<String> nomesProdutos = v.getItens().stream()
                .map(i -> i.getQuantidade() + "x " + i.getProduto().getNome())
                .collect(Collectors.toList());

            return new RelatorioVendaDTO(
                    v.getId(),
                    v.getData(),
                    v.getCliente().getNome(),
                    v.getValorTotal(),
                    v.getUsuario().getUsername(),
                    nomesProdutos
            );
        }).collect(Collectors.toList());
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }
}
