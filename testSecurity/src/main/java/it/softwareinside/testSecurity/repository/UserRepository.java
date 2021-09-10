package it.softwareinside.testSecurity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.softwareinside.testSecurity.model.User;

public interface UserRepository extends MongoRepository<User, String>{

}
