package com.hemoplay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.model.Notificacao;
import com.hemoplay.repository.NotificacaoRepository;
import com.hemoplay.repository.UsuarioRepository;

@Service
@Transactional
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository, UsuarioRepository usuarioRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Notificacao> listar(Long idUsuario) {
        conferirUsuario(idUsuario);
        return notificacaoRepository.findByIdUsuarioOrderByDataEnvioDesc(idUsuario);
    }

    public void marcarTodasComoLidas(Long idUsuario) {
        conferirUsuario(idUsuario);
        notificacaoRepository.findByIdUsuarioOrderByDataEnvioDesc(idUsuario)
                .forEach(Notificacao::marcarComoLida);
    }

    private void conferirUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
    }
}
