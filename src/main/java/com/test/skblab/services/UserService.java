package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.database.repositories.UserRepository;
import com.test.skblab.exceptions.UserExistsException;
import com.test.skblab.models.UserRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    User addUser(UserRequestData userRequestData) throws UserExistsException {
        User user = userRepository
                .findUserByLogin(userRequestData.getLogin())
                .orElse(null);

        if (user != null) {
            String message = "User " + userRequestData.getLogin() + " already exist";
            log.info(message);
            throw new UserExistsException(message);
        }

        user = new User();
        user.setLogin(userRequestData.getLogin());
        //пароль лучше хранить encoded
        user.setPassword(userRequestData.getPassword());
        user.setEmail(userRequestData.getEmail());
        user.setSecondName(userRequestData.getSecondName());
        user.setFirstName(userRequestData.getFirstName());
        user.setMiddleName(userRequestData.getMiddleName());

        return userRepository.save(user);
    }

    void updateUser(User user) {
        userRepository.save(user);
    }

}
