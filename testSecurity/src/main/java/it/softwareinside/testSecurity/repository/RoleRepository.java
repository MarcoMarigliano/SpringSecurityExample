package it.softwareinside.testSecurity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.softwareinside.testSecurity.model.Role;



@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
	 Role findByRole(String role);
}