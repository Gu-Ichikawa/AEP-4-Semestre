package com.hemoplay.controller;

import com.hemoplay.dto.TipoSanguineoResponse;
import com.hemoplay.service.TipoSanguineoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TipoSanguineoController {

    private final TipoSanguineoService tipoSanguineoService;

    public TipoSanguineoController(TipoSanguineoService tipoSanguineoService) {
        this.tipoSanguineoService = tipoSanguineoService;
    }

    @GetMapping("/tipos-sanguineos")
    public List<TipoSanguineoResponse> listar() {
        return tipoSanguineoService.listar().stream()
                .map(TipoSanguineoResponse::from)
                .toList();
    }
}
