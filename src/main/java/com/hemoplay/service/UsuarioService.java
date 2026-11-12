package com.hemoplay.service;

import com.hemoplay.model.Doador;
import com.hemoplay.model.Nivel;
import com.hemoplay.model.TipoSanguineo;
import com.hemoplay.model.Usuario;
import com.hemoplay.repository.NivelRepository;
import com.hemoplay.repository.TipoSanguineoRepository;
import com.hemoplay.repository.UsuarioRepository;
import java.time.LocalDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private static final String NIVEL_INICIAL = "Iniciante";

    private final UsuarioRepository usuarioRepository;
    private final TipoSanguineoRepository tipoSanguineoRepository;
    private final NivelRepository nivelRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoSanguineoRepository tipoSanguineoRepository,
                          NivelRepository nivelRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoSanguineoRepository = tipoSanguineoRepository;
        this.nivelRepository = nivelRepository;
    }

    // RF01: cadastro de doador. O tipo sanguíneo é opcional (RF02).
    public Usuario cadastrar(String nome, String email, String senha,
                             LocalDate dataNasc, String telefone, Long idTipoSanguineo) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Doador doador = new Doador();
        doador.setNome(nome);
        doador.setEmail(email);
        doador.setSenha(encoder.encode(senha));   // guarda o hash, nunca a senha pura
        doador.setDataNasc(dataNasc);
        doador.setTelefone(telefone);
        doador.setTipoSanguineo(buscarTipoSanguineo(idTipoSanguineo));
        doador.setNivel(buscarNivelInicial());

        return usuarioRepository.save(doador);
    }

    // RF02: consulta do perfil
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    // RF02: atualização do perfil. E-mail e senha não mudam por aqui.
    public Usuario atualizarPerfil(Long idUsuario, String nome, String telefone,
                                   LocalDate dataNasc, Long idTipoSanguineo) {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setDataNasc(dataNasc);
        usuario.setTipoSanguineo(buscarTipoSanguineo(idTipoSanguineo));
        return usuario; // o JPA grava as alterações ao fim da transação
    }

    // CRUD: exclusão. As doações do usuário são removidas pelo ON DELETE CASCADE do banco.
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