package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.repositories.TypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas aisladas del servicio: no necesitan conexion a base de datos. */
@ExtendWith(MockitoExtension.class)
class TypeServiceMockitoTest {
    @Mock private TypeRepository repository;
    @InjectMocks private TypeServiceImpl service;

    private Type sample(Integer id) {
        Type type = new Type();
        type.setId(id);
        type.setName("Hamster");
        return type;
    }

    @Test
    void testCreateType() {
        Type input = sample(null);
        Type saved = sample(20);
        saved.setActive(true);
        when(repository.save(input)).thenReturn(saved);
        assertSame(saved, service.create(input));
        assertTrue(input.getActive());
        verify(repository).save(input);
    }

    @Test
    void testUpdateType() {
        Type existing = sample(20);
        Type updated = sample(20);
        updated.setName("Hamster actualizado");
        when(repository.findById(20)).thenReturn(Optional.of(existing));
        when(repository.save(updated)).thenReturn(updated);
        assertEquals("Hamster actualizado", service.update(updated).getName());
        verify(repository).save(updated);
    }

    @Test
    void testFindTypeById() {
        Type existing = sample(20);
        when(repository.findById(20)).thenReturn(Optional.of(existing));
        assertSame(existing, service.findById(20));
        verify(repository).findById(20);
    }

    @Test
    void testDeleteType() {
        Type existing = sample(20);
        when(repository.findById(20)).thenReturn(Optional.of(existing));
        service.delete(20);
        verify(repository).delete(existing);
    }

    @Test
    void testFindTypeById_NotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());
        assertThrows(TypeNotFoundException.class, () -> service.findById(999));
    }

    @Test
    void testDeleteType_NotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());
        assertThrows(TypeNotFoundException.class, () -> service.delete(999));
        verify(repository, never()).delete(any());
    }

    @Test
    void testUpdateType_NotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());
        assertThrows(TypeNotFoundException.class, () -> service.update(sample(999)));
        verify(repository, never()).save(any());
    }

    @Test
    void testCreateType_WithIdRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.create(sample(20)));
        verifyNoInteractions(repository);
    }
}
