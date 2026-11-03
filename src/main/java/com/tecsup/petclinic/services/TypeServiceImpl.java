package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TypeServiceImpl implements TypeService {
    private final TypeRepository repository;

    @Override
    public Type create(Type type) {
        if (type.getId() != null) {
            throw new IllegalArgumentException("La creacion requiere un ID nulo");
        }
        if (type.getActive() == null) {
            type.setActive(true);
        }
        return repository.save(type);
    }

    @Override
    public Type update(Type type) {
        findById(type.getId());
        return repository.save(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Type findById(Integer id) {
        if (id == null) {
            throw new TypeNotFoundException(null);
        }
        return repository.findById(id).orElseThrow(() -> new TypeNotFoundException(id));
    }

    @Override
    public void delete(Integer id) {
        repository.delete(findById(id));
    }
}
