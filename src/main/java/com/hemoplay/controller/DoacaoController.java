package com.hemoplay.controller;

import com.hemoplay.dto.ConfirmarDoacaoRequest;
import com.hemoplay.dto.DoacaoRequest;
import com.hemoplay.dto.DoacaoResponse;
import com.hemoplay.model.Doacao;
import com.hemoplay.service.DoacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    // RF05: o doador registra a doação (status REGISTRADA)
    @PostMapping("/doacoes")
    public ResponseEntity<DoacaoResponse> registrar(@Valid @RequestBody DoacaoRequest request) {
        Doacao doacao = doacaoService.registrar(request.idUsuario(), request.idLocal());
        return ResponseEntity.status(HttpStatus.CREATED).body(DoacaoResponse.from(doacao));
    }

    // RF05: só administrador confirma (status CONFIRMADA)
    @PatchMapping("/doacoes/{id}/confirmar")
    public DoacaoResponse confirmar(@PathVariable Long id,
                                    @Valid @RequestBody ConfirmarDoacaoRequest request) {
        Doacao doacao = doacaoService.confirmar(id, request.idAdministrador());
        return DoacaoResponse.from(doacao);
    }

    // RF06: histórico do usuário
    @GetMapping("/usuarios/{idUsuario}/doacoes")
    public List<DoacaoResponse> historico(@PathVariable Long idUsuario) {
        return doacaoService.historico(idUsuario).stream()
                .map(DoacaoResponse::from)
                .toList();
    }
}