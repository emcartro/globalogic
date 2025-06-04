package com.globallogic.app.users_app.repository;

import com.globallogic.app.users_app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz Repository encargada de realizar las transacciones hacia la base de datos del entity {@link User}.
 * @author </br>
    * Developer: Emilio Carcamo.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
	Optional<User> findByEmail(String email);
	
}
