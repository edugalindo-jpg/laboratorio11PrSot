package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

}
