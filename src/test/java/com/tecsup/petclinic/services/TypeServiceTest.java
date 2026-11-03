package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TypeServiceTest {

    @Autowired
    private TypeService typeService;

    @Autowired
    private TypeMapper typeMapper;

    @MockitoBean
    private TypeRepository repository;

    /**
     *
     */
    @Test
    public void testCreateType() {

        Type newType = new Type(null, "ferret", "Small mammal", true, "small", 8, "medium");
        Type newTypeCreated = new Type(100, "ferret", "Small mammal", true, "small", 8, "medium");

        TypeDTO newTypeDTO = this.typeMapper.mapToDto(newType);

        Mockito.when(this.repository.save(newType))
                .thenReturn(newTypeCreated);

        TypeDTO typeDTOCreated = this.typeService.create(newTypeDTO);

        log.info("Type created : {}", typeDTOCreated);

        assertNotNull(typeDTOCreated.getId());
        assertEquals(newTypeCreated.getName(), typeDTOCreated.getName());
        assertEquals(newTypeCreated.getDescription(), typeDTOCreated.getDescription());
    }

    /**
     *
     */
    @Test
    public void testUpdateType() {

        String UP_TYPE_NAME = "parrot";
        String UP_DESCRIPTION = "Talking bird";

        Type typeCreated = new Type(200, "bird", "Avian", true, "small", 8, "medium");

        TypeDTO typeDTO = this.typeMapper.mapToDto(typeCreated);

        // Prepare data for update
        typeDTO.setName(UP_TYPE_NAME);
        typeDTO.setDescription(UP_DESCRIPTION);

        Type typeUpdate = this.typeMapper.mapToEntity(typeDTO);

        Mockito.when(this.repository.save(typeUpdate))
                .thenReturn(typeUpdate);

        // Execute update
        TypeDTO typeDTOUpdate = this.typeService.update(typeDTO);
        log.info("Type updated : {}", typeDTOUpdate);

        //            EXPECTED           ACTUAL
        assertEquals(UP_TYPE_NAME, typeDTOUpdate.getName());
        assertEquals(UP_DESCRIPTION, typeDTOUpdate.getDescription());
    }

    /**
     *
     */
    @Test
    public void testFindTypeById() {

        Type typeExpected = new Type(1, "cat", "Domestic feline", true, "small", 15, "medium");

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(typeExpected));

        TypeDTO type = null;

        try {
            type = this.typeService.findById(1);
        } catch (TypeNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("Type found : {}", type);

        assertEquals(typeExpected.getName(), type.getName());
        assertEquals(typeExpected.getCareLevel(), type.getCareLevel());
    }

    @Test
    public void testGetPetCountByType() {
        Type type = new Type(null, "test-type", "Test type", true, "small", 8, "low");
        Type savedType = new Type(100, "test-type", "Test type", true, "small", 8, "low");
        TypeDTO typeDTO = typeMapper.mapToDto(type);

        Mockito.when(repository.save(type)).thenReturn(savedType);
        Mockito.when(repository.getPetCountByType())
                .thenReturn(List.<Object[]>of(new Object[]{"test-type", 2L, true}));

        TypeDTO createdType = typeService.create(typeDTO);
        List<PetCountByTypeDTO> report = typeService.getPetCountByType();

        assertNotNull(report);
        assertTrue(report.size() > 0);
        assertEquals("test-type", report.get(0).getTypeName());
        assertEquals(2L, report.get(0).getPetCount());
        assertTrue(report.get(0).getActive());
        assertEquals(100, createdType.getId());
        Mockito.verify(repository).getPetCountByType();
    }

    @Test
    public void testGetPetCountByType_Empty() {
        Mockito.when(repository.getPetCountByType()).thenReturn(List.of());

        List<PetCountByTypeDTO> report = typeService.getPetCountByType();

        assertNotNull(report);
        assertInstanceOf(List.class, report);
        assertTrue(report.isEmpty());
        assertEquals(0, report.size());
        Mockito.verify(repository).getPetCountByType();
    }

    @Test
    public void testDeleteType() {
        Integer typeId = 300;
        Type type = new Type(typeId, "temporary-type", "Temporary type", true, "small", 5, "low");
        Mockito.when(repository.findById(typeId))
                .thenReturn(Optional.of(type), Optional.empty());

        assertDoesNotThrow(() -> typeService.delete(typeId));
        assertThrows(TypeNotFoundException.class, () -> typeService.findById(typeId));

        Mockito.verify(repository).delete(type);
        Mockito.verify(repository, Mockito.times(2)).findById(typeId);
    }

}
