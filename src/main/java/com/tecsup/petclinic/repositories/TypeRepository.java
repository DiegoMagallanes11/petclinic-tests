package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Integer> {
}
