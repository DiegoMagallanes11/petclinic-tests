package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.*;
import com.tecsup.petclinic.exceptions.*;
import com.tecsup.petclinic.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Grupo 5: logica aislada con JUnit 5 y Mockito. */
@ExtendWith(MockitoExtension.class)
class VetSpecialtyServiceMockitoTest {
    @Mock private VetSpecialtyRepository repository;
    @Mock private VetRepository vets;
    @Mock private SpecialtyRepository specialties;
    @InjectMocks private VetSpecialtyServiceImpl service;
    private final VetSpecialtyId id = new VetSpecialtyId(101, 201);

    private void lockVet() {
        Vet vet = new Vet();
        vet.setId(101);
        when(vets.findByIdForUpdate(101)).thenReturn(Optional.of(vet));
    }

    private VetSpecialty existing() {
        lockVet();
        VetSpecialty assignment = new VetSpecialty(id);
        when(repository.findById(id)).thenReturn(Optional.of(assignment));
        return assignment;
    }

    @Test
    void testAssignSpecialtyToVet() {
        lockVet();
        when(specialties.existsById(201)).thenReturn(true);
        when(repository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        VetSpecialty saved = service.assignSpecialtyToVet(101, 201);
        assertEquals(id, saved.getId());
        assertEquals(0, saved.getYearsExperience());
        assertFalse(saved.isPrimarySpecialty());
        assertTrue(saved.isNew());
        verify(repository).saveAndFlush(saved);
    }

    @Test
    void testRemoveSpecialtyFromVet() {
        VetSpecialty assignment = existing();
        service.removeSpecialtyFromVet(101, 201);
        verify(repository).delete(assignment);
    }

    @Test
    void testAssignDuplicate_ShouldFail() {
        lockVet();
        when(specialties.existsById(201)).thenReturn(true);
        when(repository.existsById(id)).thenReturn(true);
        assertThrows(DuplicateVetSpecialtyException.class, () -> service.assignSpecialtyToVet(101, 201));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void testFindSpecialtiesByVet() {
        Specialty specialty = new Specialty();
        specialty.setId(201);
        specialty.setName("Especialidad de Angel");
        when(repository.findSpecialtiesByVet(101)).thenReturn(List.of(specialty));
        assertEquals(List.of(specialty), service.findSpecialtiesByVet(101));
        verify(repository).findSpecialtiesByVet(101);
    }

    @Test
    void testFindVetsBySpecialty() {
        Vet vet = new Vet();
        vet.setId(101);
        when(repository.findVetsBySpecialty(201)).thenReturn(List.of(vet));
        assertEquals(List.of(vet), service.findVetsBySpecialty(201));
        verify(repository).findVetsBySpecialty(201);
    }

    @Test
    void testFindSpecialtiesByVet_Empty() {
        when(repository.findSpecialtiesByVet(101)).thenReturn(List.of());
        assertTrue(service.findSpecialtiesByVet(101).isEmpty());
    }

    @Test
    void testSetPrimarySpecialty() {
        VetSpecialty assignment = existing();
        when(repository.findByIdVetId(101)).thenReturn(List.of(assignment));
        assertSame(assignment, service.setPrimarySpecialty(101, 201));
        assertTrue(assignment.isPrimarySpecialty());
        verify(repository).saveAll(List.of(assignment));
    }

    @Test
    void testSetPrimarySpecialty_OnlyOnePrimary() {
        VetSpecialty target = existing();
        VetSpecialty previous = new VetSpecialty(new VetSpecialtyId(101, 202));
        previous.setPrimarySpecialty(true);
        List<VetSpecialty> assignments = List.of(target, previous);
        when(repository.findByIdVetId(101)).thenReturn(assignments);
        service.setPrimarySpecialty(101, 201);
        assertTrue(target.isPrimarySpecialty());
        assertFalse(previous.isPrimarySpecialty());
        assertEquals(1, assignments.stream().filter(VetSpecialty::isPrimarySpecialty).count());
        verify(repository).saveAll(assignments);
    }

    @Test
    void testSetPrimarySpecialty_NotAssigned() {
        lockVet();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VetSpecialtyNotFoundException.class, () -> service.setPrimarySpecialty(101, 201));
        verify(repository, never()).findByIdVetId(any());
        verify(repository, never()).saveAll(any());
    }

    @Test
    void testFindVetsByMinExperience() {
        Vet vet = new Vet();
        vet.setId(101);
        when(repository.findVetsByMinExperience(201, 5)).thenReturn(List.of(vet));
        assertEquals(List.of(vet), service.findVetsByMinExperience(201, 5));
        verify(repository).findVetsByMinExperience(201, 5);
    }

    @Test
    void testUpdateYearsExperience() {
        VetSpecialty assignment = existing();
        when(repository.save(assignment)).thenReturn(assignment);
        assertSame(assignment, service.updateYearsExperience(101, 201, 8));
        assertEquals(8, assignment.getYearsExperience());
        verify(repository).save(assignment);
    }

    @Test
    void testUpdateCertificationDate() {
        VetSpecialty assignment = existing();
        LocalDate date = LocalDate.of(2024, 6, 15);
        when(repository.save(assignment)).thenReturn(assignment);
        assertSame(assignment, service.updateCertificationDate(101, 201, date));
        assertEquals(date, assignment.getCertificationDate());
        verify(repository).save(assignment);
    }

    @Test
    void testAssignSpecialtyToVet_MissingVet() {
        when(vets.findByIdForUpdate(101)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.assignSpecialtyToVet(101, 201));
        verifyNoInteractions(repository, specialties);
    }

    @Test
    void testAssignSpecialtyToVet_MissingSpecialty() {
        lockVet();
        when(specialties.existsById(201)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.assignSpecialtyToVet(101, 201));
        verifyNoInteractions(repository);
    }

    @Test
    void testRemoveSpecialtyFromVet_NotAssigned() {
        lockVet();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VetSpecialtyNotFoundException.class, () -> service.removeSpecialtyFromVet(101, 201));
        verify(repository, never()).delete(any());
    }

    @Test
    void testRejectNegativeExperience() {
        assertThrows(IllegalArgumentException.class, () -> service.updateYearsExperience(101, 201, -1));
        assertThrows(IllegalArgumentException.class, () -> service.findVetsByMinExperience(201, -1));
        verifyNoInteractions(repository, vets);
    }

    @Test
    void testRejectMissingParameters() {
        assertThrows(IllegalArgumentException.class, () -> service.assignSpecialtyToVet(null, 201));
        assertThrows(IllegalArgumentException.class, () -> service.assignSpecialtyToVet(101, null));
        assertThrows(IllegalArgumentException.class, () -> service.findSpecialtiesByVet(null));
        assertThrows(IllegalArgumentException.class, () -> service.findVetsBySpecialty(null));
        assertThrows(IllegalArgumentException.class, () -> service.findVetsByMinExperience(null, 0));
        assertThrows(IllegalArgumentException.class, () -> service.updateCertificationDate(101, 201, null));
        verifyNoInteractions(repository, vets, specialties);
    }
}
