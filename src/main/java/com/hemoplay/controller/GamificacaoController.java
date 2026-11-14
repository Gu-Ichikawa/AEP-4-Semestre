package com.hemoplay.controller;

import com.hemoplay.dto.ConquistaResponse;
import com.hemoplay.dto.MissaoResponse;
import com.hemoplay.dto.NotificacaoResponse;
import com.hemoplay.service.ConquistaService;
import com.hemoplay.service.MissaoService;
import com.hemoplay.service.NotificacaoService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios/{idUsuario}")
public class GamificacaoController {

    private final MissaoService missaoService;
    private final ConquistaService conquistaService;
    private final NotificacaoService notificacaoService;

    public GamificacaoController(MissaoService missaoService,
                                 ConquistaService conquistaService,
                                 NotificacaoService notificacaoService) {
        this.missaoService = missaoService;
        this.conquistaService = conquistaService;
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/missoes")
    public List<MissaoResponse> listarMissoes(@PathVariable Long idUsuario) {
        return missaoService.listar(idUsuario);
    }

    @PostMapping("/missoes/{idMissao}/concluir")
    public MissaoResponse concluirMissao(@PathVariable Long idUsuario, @PathVariable Long idMissao) {
        return missaoService.concluirPorVisita(idUsuario, idMissao);
    }

    @GetMapping("/conquistas")
    public List<ConquistaResponse> listarConquistas(@PathVariable Long idUsuario) {
        return conquistaService.listar(idUsuario);
    }

    @GetMapping("/notificacoes")
    public List<NotificacaoResponse> listarNotificacoes(@PathVariable Long idUsuario) {
        return notificacaoService.listar(idUsuario).stream().map(NotificacaoResponse::from).toList();
    }

    @PatchMapping("/notificacoes/lidas")
    public ResponseEntity<Void> marcarNotificacoesComoLidas(@PathVariable Long idUsuario) {
        notificacaoService.marcarTodasComoLidas(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
