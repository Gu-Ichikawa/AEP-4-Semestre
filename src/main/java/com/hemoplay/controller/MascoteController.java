package com.hemoplay.controller;

import com.hemoplay.dto.EquiparVisualRequest;
import com.hemoplay.dto.MascoteResponse;
import com.hemoplay.service.MascoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MascoteController {

    private final MascoteService mascoteService;

    public MascoteController(MascoteService mascoteService) {
        this.mascoteService = mascoteService;
    }

    @GetMapping("/usuarios/{idUsuario}/mascote")
    public MascoteResponse consultar(@PathVariable Long idUsuario) {
        return mascoteService.consultar(idUsuario);
    }

    @PutMapping("/usuarios/{idUsuario}/mascote")
    public MascoteResponse equipar(@PathVariable Long idUsuario,
                                   @Valid @RequestBody EquiparVisualRequest request) {
        return mascoteService.equipar(idUsuario, request.idItem());
    }
}
