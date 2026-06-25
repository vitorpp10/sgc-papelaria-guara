package br.com.sgc.controller;

import br.com.sgc.domain.model.Venda;
import br.com.sgc.dto.RelatorioVendaDTO;
import br.com.sgc.dto.VendaDTO;
import br.com.sgc.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Padrão RestController para criar a API que se comunicará via JSON
@RestController
@RequestMapping("/vendas")
@CrossOrigin("*") // Permite acesso do frontend
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<?> registrarVenda(@RequestBody VendaDTO vendaDTO) {
        try {
            Venda venda = vendaService.registrarVenda(vendaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(venda);
        } catch (RuntimeException e) {
            // Em caso de falta de estoque, retorna um Bad Request explicando o erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    // Endpoint para relatório: GET /vendas/relatorio?inicio=2023-01-01&fim=2023-12-31
    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioVendaDTO>> gerarRelatorio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        // Converte as datas simples para inicio e fim do dia para a busca no banco
        LocalDateTime dataInicio = inicio.atStartOfDay();
        LocalDateTime dataFim = fim.atTime(23, 59, 59);

        List<RelatorioVendaDTO> relatorio = vendaService.gerarRelatorio(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
