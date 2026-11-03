package com.tecsup.petclinic.exceptions;

public class TypeNotFoundException extends RuntimeException {
    public TypeNotFoundException(Integer id) {
        super("Tipo de mascota no encontrado: " + id);
    }
}
