package com.hemoplay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.dto.MascoteResponse;
import com.hemoplay.dto.VisualMascoteResponse;
import com.hemoplay.model.Inventario;
import com.hemoplay.model.Item;
import com.hemoplay.model.Usuario;
import com.hemoplay.model.requisito.ProgressoUsuario;
import com.hemoplay.model.requisito.RequisitoDesbloqueio;
import com.hemoplay.repository.InventarioRepository;
import com.hemoplay.repository.ItemRepository;
import com.hemoplay.repository.UsuarioRepository;

@Service
@Transactional
public class MascoteService {

    public static final String VISUAL_PADRAO = "Cor vermelha";

    private final ItemRepository itemRepository;
    private final InventarioRepository inventarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final GamificacaoService gamificacaoService;

    public MascoteService(ItemRepository itemRepository,
                          InventarioRepository inventarioRepository,
                          UsuarioRepository usuarioRepository,
                          GamificacaoService gamificacaoService) {
        this.itemRepository = itemRepository;
        this.inventarioRepository = inventarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.gamificacaoService = gamificacaoService;
    }

    @Transactional(readOnly = true)
    public MascoteResponse consultar(Long idUsuario) {
        return montar(buscarUsuario(idUsuario));
    }

    public MascoteResponse equipar(Long idUsuario, Long idItem) {
        Usuario usuario = buscarUsuario(idUsuario);
        Item item = itemRepository.findById(idItem)
                .orElseThrow(() -> new IllegalArgumentException("Visual não encontrado"));

        RequisitoDesbloqueio requisito = item.obterRequisito();
        if (!requisito.estaCumprido(gamificacaoService.progressoDe(usuario))) {
            throw new IllegalStateException("Visual bloqueado. " + requisito.descricao() + ".");
        }

        if (item.ehCor()) {
            usuario.setMascoteCor(item.getNome());
            usuario.setMascoteAcessorios(null);
        } else {
            usuario.setMascoteAcessorios(item.getNome());
        }
        registrarNoInventario(usuario, item);
        gamificacaoService.aoPersonalizarMascote(usuario, !item.ehCor());
        return montar(usuario);
    }

    private void registrarNoInventario(Usuario usuario, Item item) {
        List<Inventario> inventario = inventarioRepository.findByIdUsuario(usuario.getIdUsuario());
        inventario.forEach(Inventario::desequipar);
        Inventario registro = inventarioRepository
                .findByIdUsuarioAndIdItem(usuario.getIdUsuario(), item.getIdItem())
                .orElseGet(() -> new Inventario(usuario.getIdUsuario(), item.getIdItem()));
        registro.equipar();
        inventarioRepository.save(registro);
    }

    private MascoteResponse montar(Usuario usuario) {
        ProgressoUsuario progresso = gamificacaoService.progressoDe(usuario);
        String equipado = nomeDoVisualEquipado(usuario);

        List<VisualMascoteResponse> visuais = itemRepository
                .findByTipoInOrderByIdItemAsc(List.of(Item.COR, Item.ACESSORIO)).stream()
                .map(item -> VisualMascoteResponse.from(item,
                        item.obterRequisito().estaCumprido(progresso),
                        item.getNome().equals(equipado)))
                .toList();

        VisualMascoteResponse atual = visuais.stream()
                .filter(VisualMascoteResponse::equipado)
                .findFirst()
                .orElse(visuais.isEmpty() ? null : visuais.get(0));
        return new MascoteResponse(atual, visuais);
    }

    private String nomeDoVisualEquipado(Usuario usuario) {
        if (usuario.getMascoteAcessorios() != null && !usuario.getMascoteAcessorios().isBlank()) {
            return usuario.getMascoteAcessorios();
        }
        if (usuario.getMascoteCor() != null && itemRepository.findByNome(usuario.getMascoteCor()).isPresent()) {
            return usuario.getMascoteCor();
        }
        return VISUAL_PADRAO;
    }

    private Usuario buscarUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}
