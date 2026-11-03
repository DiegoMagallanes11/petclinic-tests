package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Type;

public interface TypeService {
    Type create(Type type);
    Type update(Type type);
    Type findById(Integer id);
    void delete(Integer id);
}
