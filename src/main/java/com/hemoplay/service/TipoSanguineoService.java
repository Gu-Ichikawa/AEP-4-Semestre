package com.hemoplay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.model.TipoSanguineo;
import com.hemoplay.repository.TipoSanguineoRepository;

@Service
@Transactional(readOnly = true)
public class TipoSanguineoService {

    private final TipoSanguineoRepository tipoSanguineoRepository;

    public TipoSanguineoService(TipoSanguineoRepository tipoSanguineoRepository) {
        this.tipoSanguineoRepository = tipoSanguineoRepository;
    }

    public List<TipoSanguineo> listar() {
        return tipoSanguineoRepository.findAll();
    }
}
