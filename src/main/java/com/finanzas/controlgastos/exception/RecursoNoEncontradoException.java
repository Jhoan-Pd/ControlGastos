package com.finanzas.controlgastos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Excepción lanzada cuando no se encuentra un recurso por su identificador
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
