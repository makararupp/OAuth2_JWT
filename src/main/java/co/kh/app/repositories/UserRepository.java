package co.kh.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.kh.app.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByEmail(String email);
    
 }
