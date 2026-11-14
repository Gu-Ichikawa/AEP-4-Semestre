package com.hemoplay.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.model.Conquista;
import com.hemoplay.model.Doacao;
import com.hemoplay.model.Evento;
import com.hemoplay.model.Missao;
import com.hemoplay.model.Nivel;
import com.hemoplay.model.Notificacao;
import com.hemoplay.model.ParticipacaoMissao;
import com.hemoplay.model.StatusDoacao;
import com.hemoplay.model.Usuario;
import com.hemoplay.model.UsuarioConquista;
import com.hemoplay.model.UsuarioConquistaId;
import com.hemoplay.model.requisito.ProgressoUsuario;
import com.hemoplay.repository.ConquistaRepository;
import com.hemoplay.repository.DoacaoRepository;
import com.hemoplay.repository.EventoRepository;
import com.hemoplay.repository.MissaoRepository;
import com.hemoplay.repository.NivelRepository;
import com.hemoplay.repository.NotificacaoRepository;
import com.hemoplay.repository.ParticipacaoMissaoRepository;
import com.hemoplay.repository.UsuarioConquistaRepository;

@Service
@Transactional
public class GamificacaoService {

    public static final int PONTOS_POR_DOACAO_CONFIRMADA = 100;

    private final MissaoRepository missaoRepository;
    private final ParticipacaoMissaoRepository participacaoRepository;
    private final ConquistaRepository conquistaRepository;
    private final UsuarioConquistaRepository usuarioConquistaRepository;
    private final NivelRepository nivelRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final DoacaoRepository doacaoRepository;
    private final EventoRepository eventoRepository;

    public GamificacaoService(MissaoRepository missaoRepository,
                              ParticipacaoMissaoRepository participacaoRepository,
                              ConquistaRepository conquistaRepository,
                              UsuarioConquistaRepository usuarioConquistaRepository,
                              NivelRepository nivelRepository,
                              NotificacaoRepository notificacaoRepository,
                              DoacaoRepository doacaoRepository,
                              EventoRepository eventoRepository) {
        this.missaoRepository = missaoRepository;
        this.participacaoRepository = participacaoRepository;
        this.conquistaRepository = conquistaRepository;
        this.usuarioConquistaRepository = usuarioConquistaRepository;
        this.nivelRepository = nivelRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.doacaoRepository = doacaoRepository;
        this.eventoRepository = eventoRepository;
    }

    public void aoAtualizarCadastro(Usuario usuario) {
        if (usuario.getTipoSanguineo() != null) {
            concluirMissao(usuario, "Completar o cadastro");
            desbloquearConquista(usuario, "Primeiro Passo");
        }
    }

    public void aoRegistrarDoacao(Usuario usuario, Doacao doacao) {
        concluirMissao(usuario, "Registrar uma doação");
        if (aconteceuDuranteEvento(doacao.getDataRegistro().toLocalDate())) {
            concluirMissao(usuario, "Doação do mês do doador");
        }
    }

    public void aoConfirmarDoacao(Usuario doador) {
        adicionarPontos(doador, PONTOS_POR_DOACAO_CONFIRMADA, "Doação confirmada pelo hemocentro.");
        concluirMissao(doador, "Doação confirmada");
        desbloquearConquista(doador, "Primeira Doação");

        List<Doacao> confirmadas = doacaoRepository
                .findByUsuarioIdUsuarioAndStatus(doador.getIdUsuario(), StatusDoacao.CONFIRMADA);

        if (confirmadas.size() >= 3) {
            desbloquearConquista(doador, "Doador Constante");
        }

        int anoAtual = LocalDate.now().getYear();
        long confirmadasNoAno = confirmadas.stream()
                .filter(doacao -> doacao.getDataConfirmacao().getYear() == anoAtual)
                .count();
        if (confirmadasNoAno >= 3) {
            concluirMissao(doador, "Três doações no ano");
        }

        long confirmadasNoEvento = confirmadas.stream()
                .filter(doacao -> aconteceuDuranteEvento(doacao.getDataConfirmacao().toLocalDate()))
                .count();
        if (confirmadasNoEvento >= 2) {
            concluirMissao(doador, "Dupla doação do mês");
        }
    }

    public void aoPersonalizarMascote(Usuario usuario, boolean usouAcessorio) {
        concluirMissao(usuario, "Personalizar o mascote");
        if (usouAcessorio) {
            desbloquearConquista(usuario, "Mascote Vestido");
        }
    }

    public Missao concluirMissaoDeVisita(Usuario usuario, Long idMissao) {
        Missao missao = missaoRepository.findById(idMissao)
                .orElseThrow(() -> new IllegalArgumentException("Missão não encontrada"));
        if (!missao.concluidaAoVisitar()) {
            throw new IllegalStateException("Esta missão é concluída automaticamente pelo sistema.");
        }
        concluirMissao(usuario, missao.getNome());
        return missao;
    }

    private void concluirMissao(Usuario usuario, String nomeMissao) {
        Optional<Missao> encontrada = missaoRepository.findByNome(nomeMissao);
        if (encontrada.isEmpty()) {
            return;
        }
        Missao missao = encontrada.get();
        boolean jaConcluida = participacaoRepository
                .existsByIdUsuarioAndIdMissao(usuario.getIdUsuario(), missao.getIdMissao());
        if (jaConcluida || !missao.estaDisponivel(LocalDate.now())) {
            return;
        }

        participacaoRepository.save(new ParticipacaoMissao(usuario.getIdUsuario(), missao.getIdMissao()));
        notificar(usuario, "MISSAO", "Missão concluída",
                String.format("Você concluiu a missão %s e ganhou %d pontos.", missao.getNome(), missao.getPontuacao()));
        if (missao.getPontuacao() > 0) {
            adicionarPontos(usuario, missao.getPontuacao(), null);
        }
    }

    private void desbloquearConquista(Usuario usuario, String nomeConquista) {
        Optional<Conquista> encontrada = conquistaRepository.findByNome(nomeConquista);
        if (encontrada.isEmpty()) {
            return;
        }
        Conquista conquista = encontrada.get();
        UsuarioConquistaId id = new UsuarioConquistaId(usuario.getIdUsuario(), conquista.getIdConquista());
        if (usuarioConquistaRepository.existsById(id)) {
            return;
        }

        usuarioConquistaRepository.save(new UsuarioConquista(usuario.getIdUsuario(), conquista.getIdConquista()));
        String recompensa = conquista.getRecompensa() == null ? "" : " Recompensa: " + conquista.getRecompensa() + ".";
        notificar(usuario, "CONQUISTA", "Conquista desbloqueada",
                "Você desbloqueou " + conquista.getNome() + "." + recompensa);
    }

    private void adicionarPontos(Usuario usuario, int pontos, String motivo) {
        usuario.adicionarPontos(pontos);
        if (motivo != null) {
            notificar(usuario, "RECOMPENSA", "Você ganhou " + pontos + " pontos", motivo);
        }
        atualizarNivel(usuario);
    }

    public void atualizarNivel(Usuario usuario) {
        Nivel novoNivel = nivelPara(usuario.getPontuacao());
        if (novoNivel == null) {
            return;
        }
        Nivel nivelAtual = usuario.getNivel();
        boolean mudou = nivelAtual == null || !nivelAtual.getIdNivel().equals(novoNivel.getIdNivel());
        usuario.setNivel(novoNivel);
        if (mudou && nivelAtual != null) {
            notificar(usuario, "NIVEL", "Novo nível: " + novoNivel.getNome(),
                    "Você chegou ao nível " + novoNivel.getNome() + ". Veja os novos visuais do mascote.");
        }
    }

    public Nivel nivelPara(int pontuacao) {
        return nivelRepository.findAll().stream()
                .filter(nivel -> nivel.getPontuacaoMin() <= pontuacao)
                .max(Comparator.comparingInt(Nivel::getPontuacaoMin))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public ProgressoUsuario progressoDe(Usuario usuario) {
        Long idUsuario = usuario.getIdUsuario();

        Set<String> niveis = nivelRepository.findAll().stream()
                .filter(nivel -> nivel.getPontuacaoMin() <= usuario.getPontuacao())
                .map(Nivel::getNome)
                .collect(Collectors.toSet());

        Set<Long> idsMissoes = participacaoRepository.findByIdUsuario(idUsuario).stream()
                .filter(ParticipacaoMissao::estaConcluida)
                .map(ParticipacaoMissao::getIdMissao)
                .collect(Collectors.toSet());
        Set<String> missoes = missaoRepository.findAllById(idsMissoes).stream()
                .map(Missao::getNome)
                .collect(Collectors.toSet());

        Set<Long> idsConquistas = usuarioConquistaRepository.listarPorUsuario(idUsuario).stream()
                .map(registro -> registro.getId().getIdConquista())
                .collect(Collectors.toSet());
        Set<String> conquistas = conquistaRepository.findAllById(idsConquistas).stream()
                .map(Conquista::getNome)
                .collect(Collectors.toSet());

        Set<String> eventos = eventoRepository.findAll().stream()
                .filter(evento -> evento.estaAcontecendo(LocalDate.now()))
                .map(Evento::getNome)
                .collect(Collectors.toSet());

        return new ProgressoUsuario(niveis, missoes, conquistas, eventos);
    }

    private boolean aconteceuDuranteEvento(LocalDate data) {
        return eventoRepository.findAll().stream().anyMatch(evento -> evento.estaAcontecendo(data));
    }

    private void notificar(Usuario usuario, String tipo, String titulo, String mensagem) {
        notificacaoRepository.save(new Notificacao(usuario.getIdUsuario(), tipo, titulo, mensagem));
    }
}
