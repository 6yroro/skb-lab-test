package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.database.repositories.UserRepository;
import com.test.skblab.exceptions.UserExistsException;
import com.test.skblab.models.UserRequestData;
import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Zubkov
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final UserRequestData userRequestData;
    private final User user;
    private final String test;

    public UserServiceTest() {
        test = "test";
        userRequestData = new UserRequestData();
        userRequestData.setLogin(test);
        user = new User();
    }

    @Test
    public void addUser() throws UserExistsException {
        userService.addUser(userRequestData);
    }

    @Test(expected = UserExistsException.class)
    public void addUserExists() throws UserExistsException {
        when(userRepository.findUserByLogin(any())).thenReturn(Optional.of(user));
        userService.addUser(userRequestData);
    }

    @Test(expected = HibernateException.class)
    public void addUserDatabaseException() throws UserExistsException {
        when(userRepository.findUserByLogin(any())).thenThrow(new HibernateException(""));
        userService.addUser(userRequestData);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void addUserWithLogin() throws UserExistsException {
        user.setLogin(test);
        when(userRepository.save(refEq(user))).thenReturn(user);
        assertThat(userService.addUser(userRequestData)).isEqualTo(user);
        verify(userRepository)
                .findUserByLogin(test);
        verify(userRepository)
                .save(refEq(user));
    }
}