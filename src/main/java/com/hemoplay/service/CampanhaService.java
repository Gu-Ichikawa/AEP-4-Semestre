package com.hemoplay.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.model.Campanha;
import com.hemoplay.model.Evento;
import com.hemoplay.model.NecessidadeSanguinea;
import com.hemoplay.repository.CampanhaRepository;
import com.hemoplay.repository.EventoRepository;
import com.hemoplay.repository.NecessidadeSanguineaRepository;

@Service
@Transactional(readOnly = true)
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final NecessidadeSanguineaRepository necessidadeRepository;
    private final EventoRepository eventoRepository;

    public CampanhaService(CampanhaRepository campanhaRepository,
                           NecessidadeSanguineaRepository necessidadeRepository,
                           EventoRepository eventoRepository) {
        this.campanhaRepository = campanhaRepository;
        this.necessidadeRepository = necessidadeRepository;
        this.eventoRepository = eventoRepository;
    }

    public List<Campanha> listarCampanhas() {
        return campanhaRepository.findAllByOrderByDataInicioAsc();
    }

    public List<NecessidadeSanguinea> listarNecessidades() {
        return necessidadeRepository.findAll().stream()
                .sorted(Comparator.comparing(necessidade -> necessidade.getTipoSanguineo().getIdTipoSanguineo()))
                .toList();
    }

    public List<Evento> listarEventosAcontecendo() {
        LocalDate hoje = LocalDate.now();
        return eventoRepository.findAll().stream()
                .filter(evento -> evento.estaAcontecendo(hoje))
                .toList();
    }
}
