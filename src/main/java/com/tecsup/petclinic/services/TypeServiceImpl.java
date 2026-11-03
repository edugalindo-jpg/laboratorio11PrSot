package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetCountByTypeDTO;
import com.tecsup.petclinic.dtos.TypeDTO;
import com.tecsup.petclinic.entities.Type;
import com.tecsup.petclinic.exceptions.TypeNotFoundException;
import com.tecsup.petclinic.mappers.TypeMapper;
import com.tecsup.petclinic.repositories.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

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

	@Override
	public void delete(Integer id) throws TypeNotFoundException {
		Type type = typeRepository.findById(id)
				.orElseThrow(() -> new TypeNotFoundException("Record not found...!"));
		typeRepository.delete(type);
	}

	@Override
	public List<PetCountByTypeDTO> getPetCountByType() {
		return typeRepository.getPetCountByType().stream()
				.map(this::mapPetCountByType)
				.filter(report -> Boolean.TRUE.equals(report.getActive()))
				.toList();
	}

	private PetCountByTypeDTO mapPetCountByType(Object[] row) {
		if (row.length != 3 || !(row[0] instanceof String typeName)
				|| !(row[1] instanceof Number petCount)) {
			throw new IllegalStateException("Unexpected pet count report row");
		}

		return new PetCountByTypeDTO(typeName, petCount.longValue(), toBoolean(row[2]));
	}

	private Boolean toBoolean(Object value) {
		if (value instanceof Boolean active) {
			return active;
		}
		if (value instanceof Number active) {
			return active.intValue() != 0;
		}
		throw new IllegalStateException("Unexpected active value in pet count report");
	}
}
