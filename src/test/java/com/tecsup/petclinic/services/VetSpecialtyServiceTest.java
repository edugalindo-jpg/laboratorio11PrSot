package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jrojas
 *
 */
@Slf4j
@SpringBootTest
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Autowired
    private VetSpecialtyMapper vetSpecialtyMapper;

    @MockitoBean
    private VetSpecialtyRepository repository;

    /**
     * Asignar la especialidad 3 (dentistry) al veterinario 1 (James)
     */
    @Test
    public void testAssignSpecialtyToVet() {

        VetSpecialty newVetSpecialty = new VetSpecialty(1, 3, LocalDate.of(2020, 5, 10), 4, false, "Dental course");

        VetSpecialtyDTO newVetSpecialtyDTO = this.vetSpecialtyMapper.mapToDto(newVetSpecialty);

        Mockito.when(this.repository.save(newVetSpecialty))
                .thenReturn(newVetSpecialty);

        VetSpecialtyDTO vetSpecialtyAssigned = this.vetSpecialtyService.assign(newVetSpecialtyDTO);

        log.info("VetSpecialty assigned : {}", vetSpecialtyAssigned);

        assertNotNull(vetSpecialtyAssigned);
        assertEquals(1, vetSpecialtyAssigned.getVetId());
        assertEquals(3, vetSpecialtyAssigned.getSpecialtyId());
        assertEquals(4, vetSpecialtyAssigned.getYearsExperience());
        assertFalse(vetSpecialtyAssigned.getIsPrimary());
    }

    /**
     * El veterinario 3 (Linda) tiene surgery (2) y dentistry (3)
     */
    @Test
    public void testFindSpecialtiesByVet() {

        Integer VET_ID = 3;

        List<VetSpecialty> list = List.of(
                new VetSpecialty(3, 2, LocalDate.of(2012, 3, 20), 11, true, "Specialized in orthopedic surgery"),
                new VetSpecialty(3, 3, LocalDate.of(2018, 11, 10), 5, false, "Additional dental training"));

        Mockito.when(this.repository.findByVetId(VET_ID))
                .thenReturn(list);

        List<VetSpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVet(VET_ID);

        log.info("Specialties of vet {} : {}", VET_ID, specialties);

        assertEquals(2, specialties.size());
        assertEquals(2, specialties.get(0).getSpecialtyId());
        assertEquals(3, specialties.get(1).getSpecialtyId());

        for (VetSpecialtyDTO item : specialties) {
            assertEquals(VET_ID, item.getVetId());
        }
    }

    /**
     * La especialidad 1 (radiology) la tienen los veterinarios 2 (Helen) y 5 (Henry)
     */
    @Test
    public void testFindVetsBySpecialty() {

        Integer SPECIALTY_ID = 1;

        List<VetSpecialty> list = List.of(
                new VetSpecialty(2, 1, LocalDate.of(2015, 6, 15), 8, true, "Expert in X-ray imaging"),
                new VetSpecialty(5, 1, LocalDate.of(2016, 2, 28), 7, false, "CT and MRI certified"));

        Mockito.when(this.repository.findBySpecialtyId(SPECIALTY_ID))
                .thenReturn(list);

        List<VetSpecialtyDTO> vets = this.vetSpecialtyService.findVetsBySpecialty(SPECIALTY_ID);

        log.info("Vets of specialty {} : {}", SPECIALTY_ID, vets);

        assertEquals(2, vets.size());
        assertEquals(2, vets.get(0).getVetId());
        assertEquals(5, vets.get(1).getVetId());

        for (VetSpecialtyDTO item : vets) {
            assertEquals(SPECIALTY_ID, item.getSpecialtyId());
        }
    }

    /**
     * Quitar la especialidad 3 (dentistry) al veterinario 3 (Linda)
     */
    @Test
    public void testRemoveSpecialtyFromVet() {

        Integer VET_ID = 3;
        Integer SPECIALTY_ID = 3;

        VetSpecialty vetSpecialty = new VetSpecialty(VET_ID, SPECIALTY_ID, LocalDate.of(2018, 11, 10), 5, false, "Additional dental training");

        Mockito.when(this.repository.findById(new VetSpecialtyId(VET_ID, SPECIALTY_ID)))
                .thenReturn(Optional.of(vetSpecialty));

        assertDoesNotThrow(() -> this.vetSpecialtyService.remove(VET_ID, SPECIALTY_ID));

        Mockito.verify(this.repository).delete(vetSpecialty);
    }

    /**
     * Quitar una relacion que no existe debe lanzar excepcion
     */
    @Test
    public void testRemoveSpecialtyFromVet_NotFound() {

        Integer VET_ID = 1;
        Integer SPECIALTY_ID = 2;

        Mockito.when(this.repository.findById(new VetSpecialtyId(VET_ID, SPECIALTY_ID)))
                .thenReturn(Optional.empty());

        assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.remove(VET_ID, SPECIALTY_ID));

        Mockito.verify(this.repository, Mockito.never()).delete(Mockito.any(VetSpecialty.class));
    }

}
