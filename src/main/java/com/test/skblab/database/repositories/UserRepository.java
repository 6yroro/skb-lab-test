package com.test.skblab.database.repositories;

import com.test.skblab.database.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Alexander Zubkov
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByLogin(String login);

}
