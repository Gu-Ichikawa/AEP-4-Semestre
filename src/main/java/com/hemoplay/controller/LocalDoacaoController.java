package com.hemoplay.controller;

import com.hemoplay.dto.LocalDoacaoResponse;
import com.hemoplay.service.LocalDoacaoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocalDoacaoController {

    private final LocalDoacaoService localDoacaoService;

    public LocalDoacaoController(LocalDoacaoService localDoacaoService) {
        this.localDoacaoService = localDoacaoService;
    }

    @GetMapping("/locais")
    public List<LocalDoacaoResponse> listar() {
        return localDoacaoService.listar().stream()
                .map(LocalDoacaoResponse::from)
                .toList();
    }
}
