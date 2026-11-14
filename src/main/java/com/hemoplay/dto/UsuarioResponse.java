package com.hemoplay.dto;

import com.hemoplay.model.Usuario;
import java.time.LocalDate;
import java.util.List;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNasc,
        int pontuacao,
        String tipo,
        String tipoSanguineo,
        String nivel,
        List<String> permissoes
) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataNasc(),
                usuario.getPontuacao(),
                usuario.getClass().getSimpleName(),
                usuario.getTipoSanguineo() == null ? null : usuario.getTipoSanguineo().getRotulo(),
                usuario.getNivel() == null ? null : usuario.getNivel().getNome(),
                usuario.obterPermissoes()
        );
    }
}