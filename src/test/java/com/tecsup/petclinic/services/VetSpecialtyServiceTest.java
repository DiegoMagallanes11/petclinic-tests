package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.*;
import com.tecsup.petclinic.exceptions.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Persistencia real H2: datos propios y rollback automatico despues de cada prueba. */
@SpringBootTest
@ActiveProfiles("h2")
@Transactional
class VetSpecialtyServiceTest {
    @Autowired private VetSpecialtyService service;
    @Autowired private EntityManager em;
    @Autowired private JdbcTemplate jdbc;
    private static final int V1 = 81001, V2 = 81002, V3 = 81003;
    private static final int S1 = 82001, S2 = 82002;

    @BeforeEach
    void createOwnData() {
        for (int id : List.of(V1, V2, V3)) {
            jdbc.update("insert into vets(id,first_name,last_name,active) values (?,?,?,true)",
                    id, "Angel", "Veterinario " + id);
        }
        for (int id : List.of(S1, S2)) {
            jdbc.update("insert into specialties(id,name,office,h_open,h_close) values (?,?,?,8,17)",
                    id, "Especialidad " + id, "Laboratorio Angel");
        }
    }

    private void flushAndClear() { em.flush(); em.clear(); }

    private VetSpecialty reload(int vet, int specialty) {
        flushAndClear();
        return em.find(VetSpecialty.class, new VetSpecialtyId(vet, specialty));
    }

    @Test
    void testAssignSpecialtyToVet() {
        service.assignSpecialtyToVet(V1, S1);
        VetSpecialty saved = reload(V1, S1);
        assertNotNull(saved);
        assertEquals(new VetSpecialtyId(V1, S1), saved.getId());
        assertEquals(0, saved.getYearsExperience());
        assertFalse(saved.isPrimarySpecialty());
        assertFalse(saved.isNew());
        assertEquals("Angel", saved.getVet().getFirstName());
        assertEquals("Especialidad " + S1, saved.getSpecialty().getName());
    }

    @Test
    void testRemoveSpecialtyFromVet() {
        service.assignSpecialtyToVet(V1, S1);
        service.assignSpecialtyToVet(V1, S2);
        flushAndClear();
        service.removeSpecialtyFromVet(V1, S1);
        assertNull(reload(V1, S1));
        assertNotNull(em.find(VetSpecialty.class, new VetSpecialtyId(V1, S2)));
        assertNotNull(em.find(Vet.class, V1));
        assertNotNull(em.find(Specialty.class, S1));
    }

    @Test
    void testAssignDuplicate_ShouldFail() {
        service.assignSpecialtyToVet(V1, S1);
        service.updateYearsExperience(V1, S1, 7);
        service.updateCertificationDate(V1, S1, LocalDate.of(2020, 2, 3));
        reload(V1, S1).setNotes("Conservar esta nota");
        service.setPrimarySpecialty(V1, S1);
        flushAndClear();
        assertThrows(DuplicateVetSpecialtyException.class, () -> service.assignSpecialtyToVet(V1, S1));
        VetSpecialty saved = reload(V1, S1);
        assertEquals(7, saved.getYearsExperience());
        assertEquals(LocalDate.of(2020, 2, 3), saved.getCertificationDate());
        assertEquals("Conservar esta nota", saved.getNotes());
        assertTrue(saved.isPrimarySpecialty());
        assertEquals(1, jdbc.queryForObject("select count(*) from vet_specialties where vet_id=? and specialty_id=?",
                Integer.class, V1, S1));
    }

    @Test
    void testFindSpecialtiesByVet() {
        service.assignSpecialtyToVet(V1, S1);
        service.assignSpecialtyToVet(V1, S2);
        service.assignSpecialtyToVet(V2, S1);
        flushAndClear();
        assertEquals(List.of(S1, S2), service.findSpecialtiesByVet(V1).stream().map(Specialty::getId).toList());
    }

    @Test
    void testFindVetsBySpecialty() {
        service.assignSpecialtyToVet(V1, S1);
        service.assignSpecialtyToVet(V2, S1);
        service.assignSpecialtyToVet(V3, S2);
        flushAndClear();
        assertEquals(List.of(V1, V2), service.findVetsBySpecialty(S1).stream().map(Vet::getId).toList());
    }

    @Test
    void testFindSpecialtiesByVet_Empty() {
        service.assignSpecialtyToVet(V2, S1);
        flushAndClear();
        assertTrue(service.findSpecialtiesByVet(V1).isEmpty());
    }

    @Test
    void testSetPrimarySpecialty() {
        service.assignSpecialtyToVet(V1, S1);
        service.setPrimarySpecialty(V1, S1);
        assertTrue(reload(V1, S1).isPrimarySpecialty());
    }

    @Test
    void testSetPrimarySpecialty_OnlyOnePrimary() {
        service.assignSpecialtyToVet(V1, S1);
        service.assignSpecialtyToVet(V1, S2);
        service.assignSpecialtyToVet(V2, S1);
        service.setPrimarySpecialty(V2, S1);
        service.setPrimarySpecialty(V1, S1);
        flushAndClear();
        service.setPrimarySpecialty(V1, S2);
        // Elegir nuevamente la misma especialidad conserva una sola principal.
        service.setPrimarySpecialty(V1, S2);
        assertFalse(reload(V1, S1).isPrimarySpecialty());
        assertTrue(em.find(VetSpecialty.class, new VetSpecialtyId(V1, S2)).isPrimarySpecialty());
        assertTrue(em.find(VetSpecialty.class, new VetSpecialtyId(V2, S1)).isPrimarySpecialty());
        assertEquals(1, jdbc.queryForObject("select count(*) from vet_specialties where vet_id=? and is_primary=true",
                Integer.class, V1));
    }

    @Test
    void testSetPrimarySpecialty_NotAssigned() {
        service.assignSpecialtyToVet(V1, S1);
        service.setPrimarySpecialty(V1, S1);
        flushAndClear();
        assertThrows(VetSpecialtyNotFoundException.class, () -> service.setPrimarySpecialty(V1, S2));
        assertTrue(reload(V1, S1).isPrimarySpecialty());
        assertNull(em.find(VetSpecialty.class, new VetSpecialtyId(V1, S2)));
    }

    @Test
    void testFindVetsByMinExperience() {
        service.assignSpecialtyToVet(V1, S1);
        service.assignSpecialtyToVet(V2, S1);
        service.assignSpecialtyToVet(V3, S2);
        service.updateYearsExperience(V1, S1, 5);
        service.updateYearsExperience(V2, S1, 4);
        service.updateYearsExperience(V3, S2, 10);
        flushAndClear();
        assertEquals(List.of(V1), service.findVetsByMinExperience(S1, 5).stream().map(Vet::getId).toList());
        assertTrue(service.findVetsByMinExperience(S1, 6).isEmpty());
        assertEquals(List.of(V1, V2), service.findVetsByMinExperience(S1, 0).stream().map(Vet::getId).toList());
    }

    @Test
    void testUpdateYearsExperience() {
        service.assignSpecialtyToVet(V1, S1);
        flushAndClear();
        service.updateYearsExperience(V1, S1, 9);
        assertEquals(9, reload(V1, S1).getYearsExperience());
        service.updateYearsExperience(V1, S1, 0);
        assertEquals(0, reload(V1, S1).getYearsExperience());
    }

    @Test
    void testUpdateCertificationDate() {
        service.assignSpecialtyToVet(V1, S1);
        flushAndClear();
        LocalDate date = LocalDate.of(2024, 2, 29);
        service.updateCertificationDate(V1, S1, date);
        assertEquals(date, reload(V1, S1).getCertificationDate());
    }

    @Test
    void testCompositePrimaryKeyRejectsDuplicateInDatabase() {
        service.assignSpecialtyToVet(V1, S1);
        flushAndClear();
        assertThrows(DataIntegrityViolationException.class, () -> jdbc.update(
                "insert into vet_specialties(vet_id,specialty_id) values (?,?)", V1, S1));
    }

    @Test
    void testForeignKeyRejectsMissingSpecialtyInDatabase() {
        assertThrows(DataIntegrityViolationException.class, () -> jdbc.update(
                "insert into vet_specialties(vet_id,specialty_id) values (?,?)", V1, 82999));
    }
}
