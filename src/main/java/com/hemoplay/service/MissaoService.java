package com.hemoplay.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.dto.MissaoResponse;
import com.hemoplay.model.Missao;
import com.hemoplay.model.ParticipacaoMissao;
import com.hemoplay.model.Usuario;
import com.hemoplay.repository.MissaoRepository;
import com.hemoplay.repository.ParticipacaoMissaoRepository;
import com.hemoplay.repository.UsuarioRepository;

@Service
@Transactional
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final ParticipacaoMissaoRepository participacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GamificacaoService gamificacaoService;

    public MissaoService(MissaoRepository missaoRepository,
                         ParticipacaoMissaoRepository participacaoRepository,
                         UsuarioRepository usuarioRepository,
                         GamificacaoService gamificacaoService) {
        this.missaoRepository = missaoRepository;
        this.participacaoRepository = participacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.gamificacaoService = gamificacaoService;
    }

    @Transactional(readOnly = true)
    public List<MissaoResponse> listar(Long idUsuario) {
        buscarUsuario(idUsuario);
        Map<Long, ParticipacaoMissao> concluidas = participacaoRepository.findByIdUsuario(idUsuario).stream()
                .filter(ParticipacaoMissao::estaConcluida)
                .collect(Collectors.toMap(ParticipacaoMissao::getIdMissao, Function.identity()));

        LocalDate hoje = LocalDate.now();
        return missaoRepository.findAllByOrderByIdMissaoAsc().stream()
                .map(missao -> MissaoResponse.from(missao, concluidas.get(missao.getIdMissao()), hoje))
                .toList();
    }

    public MissaoResponse concluirPorVisita(Long idUsuario, Long idMissao) {
        Usuario usuario = buscarUsuario(idUsuario);
        Missao missao = gamificacaoService.concluirMissaoDeVisita(usuario, idMissao);
        ParticipacaoMissao participacao = participacaoRepository.findByIdUsuario(idUsuario).stream()
                .filter(registro -> registro.getIdMissao().equals(idMissao))
                .findFirst()
                .orElse(null);
        return MissaoResponse.from(missao, participacao, LocalDate.now());
    }

    private Usuario buscarUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}
