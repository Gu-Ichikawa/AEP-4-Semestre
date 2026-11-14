package com.hemoplay.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.dto.ConquistaResponse;
import com.hemoplay.model.UsuarioConquista;
import com.hemoplay.repository.ConquistaRepository;
import com.hemoplay.repository.UsuarioConquistaRepository;
import com.hemoplay.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class ConquistaService {

    private final ConquistaRepository conquistaRepository;
    private final UsuarioConquistaRepository usuarioConquistaRepository;
    private final UsuarioRepository usuarioRepository;

    public ConquistaService(ConquistaRepository conquistaRepository,
                            UsuarioConquistaRepository usuarioConquistaRepository,
                            UsuarioRepository usuarioRepository) {
        this.conquistaRepository = conquistaRepository;
        this.usuarioConquistaRepository = usuarioConquistaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ConquistaResponse> listar(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        Map<Long, UsuarioConquista> desbloqueadas = usuarioConquistaRepository.listarPorUsuario(idUsuario).stream()
                .collect(Collectors.toMap(registro -> registro.getId().getIdConquista(), Function.identity()));

        return conquistaRepository.findAllByOrderByIdConquistaAsc().stream()
                .map(conquista -> ConquistaResponse.from(conquista, desbloqueadas.get(conquista.getIdConquista())))
                .toList();
    }
}
