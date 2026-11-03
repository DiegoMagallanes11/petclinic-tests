package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/** Caso 5 del Word: CRUD persistido en H2, con rollback por prueba. */
@SpringBootTest
@ActiveProfiles("h2")
@Transactional
class TypeServiceTest {
    @Autowired private TypeService service;
    @Autowired private EntityManager entityManager;

    private Type sample() {
        Type type = new Type();
        type.setName("Hamster laboratorio");
        type.setDescription("Tipo creado por la prueba");
        type.setActive(true);
        type.setSizeCategory("small");
        type.setAverageLifespan(3);
        type.setCareLevel("medium");
        return type;
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateType() {
        Type created = service.create(sample());
        assertNotNull(created.getId());
        flushAndClear();
        assertEquals(created, service.findById(created.getId()));
    }

    @Test
    void testUpdateType() {
        Integer id = service.create(sample()).getId();
        flushAndClear();
        Type updated = service.findById(id);
        updated.setName("Hamster actualizado");
        updated.setDescription("Descripcion actualizada");
        updated.setActive(false);
        updated.setSizeCategory("medium");
        updated.setAverageLifespan(4);
        updated.setCareLevel("high");
        service.update(updated);
        flushAndClear();
        assertEquals(updated, service.findById(id));
    }

    @Test
    void testFindTypeById() {
        Type created = service.create(sample());
        flushAndClear();
        Type found = service.findById(created.getId());
        assertEquals(created, found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    void testDeleteType() {
        Integer id = service.create(sample()).getId();
        flushAndClear();
        service.delete(id);
        flushAndClear();
        assertNull(entityManager.find(Type.class, id));
        assertThrows(TypeNotFoundException.class, () -> service.findById(id));
    }
}
