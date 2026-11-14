package com.hemoplay.controller;

import com.hemoplay.dto.AtualizarUsuarioRequest;
import com.hemoplay.dto.UsuarioRequest;
import com.hemoplay.dto.UsuarioResponse;
import com.hemoplay.model.Usuario;
import com.hemoplay.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioService.cadastrar(
                request.nome(),
                request.email(),
                request.senha(),
                request.dataNasc(),
                request.telefone(),
                request.idTipoSanguineo());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(usuario));
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return UsuarioResponse.from(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id,
                                     @Valid @RequestBody AtualizarUsuarioRequest request) {
        Usuario usuario = usuarioService.atualizarPerfil(
                id,
                request.nome(),
                request.telefone(),
                request.dataNasc(),
                request.idTipoSanguineo());
        return UsuarioResponse.from(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}