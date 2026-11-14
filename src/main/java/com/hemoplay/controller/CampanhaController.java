package com.hemoplay.controller;

import com.hemoplay.dto.CampanhaResponse;
import com.hemoplay.dto.EventoResponse;
import com.hemoplay.dto.NecessidadeResponse;
import com.hemoplay.service.CampanhaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CampanhaController {

    private final CampanhaService campanhaService;

    public CampanhaController(CampanhaService campanhaService) {
        this.campanhaService = campanhaService;
    }

    @GetMapping("/campanhas")
    public List<CampanhaResponse> listarCampanhas() {
        return campanhaService.listarCampanhas().stream().map(CampanhaResponse::from).toList();
    }

    @GetMapping("/necessidades")
    public List<NecessidadeResponse> listarNecessidades() {
        return campanhaService.listarNecessidades().stream().map(NecessidadeResponse::from).toList();
    }

    @GetMapping("/eventos/ativos")
    public List<EventoResponse> listarEventosAtivos() {
        return campanhaService.listarEventosAcontecendo().stream().map(EventoResponse::from).toList();
    }
}
