package com.hemoplay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hemoplay.model.LocalDoacao;
import com.hemoplay.repository.LocalDoacaoRepository;

@Service
@Transactional(readOnly = true)
public class LocalDoacaoService {

    private final LocalDoacaoRepository localDoacaoRepository;

    public LocalDoacaoService(LocalDoacaoRepository localDoacaoRepository) {
        this.localDoacaoRepository = localDoacaoRepository;
    }

    public List<LocalDoacao> listar() {
        return localDoacaoRepository.findAll();
    }
}
