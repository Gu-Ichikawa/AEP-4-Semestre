package com.hemoplay.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hemoplay.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByNome(String nome);

    List<Item> findByTipoInOrderByIdItemAsc(Collection<String> tipos);
}
