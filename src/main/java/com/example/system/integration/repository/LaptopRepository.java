package com.example.system.integration.repository;

import com.example.system.integration.entity.Laptop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = Laptop.class, idClass = Long.class)
public interface LaptopRepository extends CrudRepository<Laptop, Long> {

	List<Laptop> findAll();

	void deleteAll();
}
