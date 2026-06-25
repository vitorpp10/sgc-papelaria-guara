package br.com.sgc.domain.repository;

import br.com.sgc.domain.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    // Spring Data JPA entende essa assinatura e cria a query automaticamente
    // Utilizado para gerar os relatórios de venda do sistema
    List<Venda> findByDataBetween(LocalDateTime inicio, LocalDateTime fim);

    // Verifica se existe alguma venda registrada para o cliente passado
    boolean existsByClienteId(Long clienteId);
}
