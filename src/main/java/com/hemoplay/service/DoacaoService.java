package com.hemoplay.service;

import com.hemoplay.model.Doacao;
import com.hemoplay.model.LocalDoacao;
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

    public DoacaoService(DoacaoRepository doacaoRepository,
                         UsuarioRepository usuarioRepository,
                         LocalDoacaoRepository localDoacaoRepository) {
        this.doacaoRepository = doacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.localDoacaoRepository = localDoacaoRepository;
    }

    // RF05: o doador registra a doação. Ela fica REGISTRADA até ser confirmada
    // por um Administrador ou por um Responsável pelo Hemocentro.
    public Doacao registrar(Long idUsuario, Long idLocal) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        LocalDoacao local = null;
        if (idLocal != null) {
            local = localDoacaoRepository.findById(idLocal)
                    .orElseThrow(() -> new IllegalArgumentException("Local não encontrado"));
        }

        return doacaoRepository.save(new Doacao(usuario, local));
    }

    // RF05: Administrador ou Responsável pelo Hemocentro podem confirmar.
    // Polimorfismo: possuiPermissao() usa o obterPermissoes() da subclasse real do usuário,
    // sem precisar perguntar qual é o tipo dele.
    public Doacao confirmar(Long idDoacao, Long idUsuarioConfirmacao) {
        Usuario quemConfirma = usuarioRepository.findById(idUsuarioConfirmacao)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!quemConfirma.possuiPermissao("confirmarDoacao")) {
            throw new IllegalStateException("Este usuário não tem permissão para confirmar doações");
        }

        Doacao doacao = doacaoRepository.findById(idDoacao)
                .orElseThrow(() -> new IllegalArgumentException("Doação não encontrada"));

        // A própria Doacao recusa uma segunda confirmação
        doacao.confirmar(LocalDateTime.now());
        return doacao; // gravado ao fim da transação
    }

    // RF06: histórico de doações do usuário, da mais recente para a mais antiga
    @Transactional(readOnly = true)
    public List<Doacao> historico(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return doacaoRepository.findByUsuarioIdUsuarioOrderByDataRegistroDesc(idUsuario);
    }
}