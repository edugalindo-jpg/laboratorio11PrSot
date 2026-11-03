package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;

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

}
