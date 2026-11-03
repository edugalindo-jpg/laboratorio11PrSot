package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author karimsovero
 *
 */
@Repository
public interface TypeRepository 
	extends JpaRepository<Type, Integer> {

	@Query(value = """
			SELECT t.name, COUNT(p.id), t.active
			FROM types t
			LEFT JOIN pets p ON p.type_id = t.id
			GROUP BY t.id, t.name, t.active
			ORDER BY t.id
			""", nativeQuery = true)
	List<Object[]> getPetCountByType();
}
