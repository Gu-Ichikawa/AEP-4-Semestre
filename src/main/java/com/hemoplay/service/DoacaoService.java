package com.hemoplay.service;

import com.hemoplay.model.Doacao;
import com.hemoplay.model.LocalDoacao;
import com.hemoplay.model.StatusDoacao;
import com.hemoplay.model.Usuario;
import com.hemoplay.repository.DoacaoRepository;
import com.hemoplay.repository.LocalDoacaoRepository;
import com.hemoplay.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalDoacaoRepository localDoacaoRepository;
    private final GamificacaoService gamificacaoService;

    public DoacaoService(DoacaoRepository doacaoRepository,
                         UsuarioRepository usuarioRepository,
                         LocalDoacaoRepository localDoacaoRepository,
                         GamificacaoService gamificacaoService) {
        this.doacaoRepository = doacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.localDoacaoRepository = localDoacaoRepository;
        this.gamificacaoService = gamificacaoService;
    }

    public Doacao registrar(Long idUsuario, Long idLocal) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        LocalDoacao local = null;
        if (idLocal != null) {
            local = localDoacaoRepository.findById(idLocal)
                    .orElseThrow(() -> new IllegalArgumentException("Local não encontrado"));
        }

        Doacao doacao = doacaoRepository.save(new Doacao(usuario, local));
        gamificacaoService.aoRegistrarDoacao(usuario, doacao);
        return doacao;
    }

    public Doacao confirmar(Long idDoacao, Long idUsuarioConfirmacao) {
        Usuario quemConfirma = usuarioRepository.findById(idUsuarioConfirmacao)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!quemConfirma.possuiPermissao("confirmarDoacao")) {
            throw new IllegalStateException("Este usuário não tem permissão para confirmar doações");
        }

        Doacao doacao = doacaoRepository.findById(idDoacao)
                .orElseThrow(() -> new IllegalArgumentException("Doação não encontrada"));

        doacao.confirmar(LocalDateTime.now());
        gamificacaoService.aoConfirmarDoacao(doacao.getUsuario());
        return doacao;
    }

    @Transactional(readOnly = true)
    public List<Doacao> listarPendentes(Long idUsuarioConsulta) {
        Usuario quemConsulta = usuarioRepository.findById(idUsuarioConsulta)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if (!quemConsulta.possuiPermissao("confirmarDoacao")) {
            throw new IllegalStateException("Este usuário não tem permissão para ver as doações pendentes");
        }
        return doacaoRepository.findByStatusOrderByDataRegistroAsc(StatusDoacao.REGISTRADA);
    }

    @Transactional(readOnly = true)
    public List<Doacao> historico(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return doacaoRepository.findByUsuarioIdUsuarioOrderByDataRegistroDesc(idUsuario);
    }
}