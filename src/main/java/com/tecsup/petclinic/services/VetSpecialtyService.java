package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

/**
 *
 * @author jrojas
 *
 */
public interface VetSpecialtyService {

	/**
	 * Asigna una especialidad a un veterinario
	 *
	 * @param vetSpecialtyDTO
	 * @return
	 */
	VetSpecialtyDTO assign(VetSpecialtyDTO vetSpecialtyDTO);

	/**
	 * Lista las especialidades de un veterinario
	 *
	 * @param vetId
	 * @return
	 */
	List<VetSpecialtyDTO> findSpecialtiesByVet(Integer vetId);

	/**
	 * Lista los veterinarios de una especialidad
	 *
	 * @param specialtyId
	 * @return
	 */
	List<VetSpecialtyDTO> findVetsBySpecialty(Integer specialtyId);

	/**
	 * Quita la especialidad a un veterinario
	 *
	 * @param vetId
	 * @param specialtyId
	 * @throws VetSpecialtyNotFoundException
	 */
	void remove(Integer vetId, Integer specialtyId) throws VetSpecialtyNotFoundException;
}
