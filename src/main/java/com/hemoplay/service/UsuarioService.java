package com.hemoplay.service;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.model.Doador;
import com.hemoplay.model.Nivel;
import com.hemoplay.model.TipoSanguineo;
import com.hemoplay.model.Usuario;
import com.hemoplay.repository.NivelRepository;
import com.hemoplay.repository.TipoSanguineoRepository;
import com.hemoplay.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    private static final String NIVEL_INICIAL = "Iniciante";

    private final UsuarioRepository usuarioRepository;
    private final TipoSanguineoRepository tipoSanguineoRepository;
    private final NivelRepository nivelRepository;
    private final GamificacaoService gamificacaoService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoSanguineoRepository tipoSanguineoRepository,
                          NivelRepository nivelRepository,
                          GamificacaoService gamificacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.tipoSanguineoRepository = tipoSanguineoRepository;
        this.nivelRepository = nivelRepository;
        this.gamificacaoService = gamificacaoService;
    }

    public Usuario cadastrar(String nome, String email, String senha,
                             LocalDate dataNasc, String telefone, Long idTipoSanguineo) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Doador doador = new Doador();
        doador.setNome(nome);
        doador.setEmail(email);
        doador.setSenha(encoder.encode(senha));
        doador.setDataNasc(dataNasc);
        doador.setTelefone(telefone);
        doador.setTipoSanguineo(buscarTipoSanguineo(idTipoSanguineo));
        doador.setNivel(buscarNivelInicial());
        doador.setMascoteCor(MascoteService.VISUAL_PADRAO);

        Usuario salvo = usuarioRepository.save(doador);
        gamificacaoService.aoAtualizarCadastro(salvo);
        return salvo;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public Usuario atualizarPerfil(Long idUsuario, String nome, String telefone,
                                   LocalDate dataNasc, Long idTipoSanguineo) {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setDataNasc(dataNasc);
        usuario.setTipoSanguineo(buscarTipoSanguineo(idTipoSanguineo));
        gamificacaoService.aoAtualizarCadastro(usuario);
        return usuario;
    }

    public void excluir(Long idUsuario) {
        usuarioRepository.delete(buscarPorId(idUsuario));
    }

    private TipoSanguineo buscarTipoSanguineo(Long idTipoSanguineo) {
        if (idTipoSanguineo == null) {
            return null;
        }
        return tipoSanguineoRepository.findById(idTipoSanguineo)
                .orElseThrow(() -> new IllegalArgumentException("Tipo sanguíneo inválido"));
    }

    private Nivel buscarNivelInicial() {
        return nivelRepository.findByNome(NIVEL_INICIAL)
                .orElseThrow(() -> new IllegalStateException(
                        "Nível inicial não cadastrado. Rode o seed.sql."));
    }
}