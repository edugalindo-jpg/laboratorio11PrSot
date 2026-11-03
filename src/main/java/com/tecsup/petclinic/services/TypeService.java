package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

import java.util.List;

/**
 * 
 * @author karimsovero
 *
 */
public interface TypeService {

	/**
	 * 
	 * @param typeDTO
	 * @return
	 */
	TypeDTO create(TypeDTO typeDTO);

	/**
	 * 
	 * @param typeDTO
	 * @return
	 */
	TypeDTO update(TypeDTO typeDTO);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws TypeNotFoundException
	 */
	TypeDTO findById(Integer id) throws TypeNotFoundException;

	void delete(Integer id) throws TypeNotFoundException;

	List<PetCountByTypeDTO> getPetCountByType();
}
