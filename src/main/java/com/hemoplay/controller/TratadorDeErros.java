package com.hemoplay.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Sem isso, qualquer erro de regra vira "500 Internal Server Error"
@RestControllerAdvice
public class TratadorDeErros {

    // Dado não encontrado ou inválido: 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> argumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", ex.getMessage()));
    }

    // Regra de negócio violada (doação já confirmada, usuário sem permissão para confirmar): 409
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> regraViolada(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erro", ex.getMessage()));
    }
}