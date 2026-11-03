package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * @author karimsovero
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

	private final TypeRepository typeRepository;
	private final TypeMapper typeMapper;

	/**
	 * 
	 * @param typeDTO
	 * @return
	 */
	@Override
	public TypeDTO create(TypeDTO typeDTO) {

		Type newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));

		return typeMapper.mapToDto(newType);
	}

	/**
	 * 
	 * @param typeDTO
	 * @return
	 */
	@Override
	public TypeDTO update(TypeDTO typeDTO) {

		Type newType = typeRepository.save(typeMapper.mapToEntity(typeDTO));

		return typeMapper.mapToDto(newType);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TypeDTO findById(Integer id) throws TypeNotFoundException {

		Optional<Type> type = typeRepository.findById(id);

		if ( !type.isPresent())
			throw new TypeNotFoundException("Record not found...!");

		return this.typeMapper.mapToDto(type.get());
	}

}
