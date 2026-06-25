package br.com.sgc.domain.repository;

import br.com.sgc.domain.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Padrão Repository: Oculta a complexidade do banco de dados fornecendo operações CRUD padrão
@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
}
