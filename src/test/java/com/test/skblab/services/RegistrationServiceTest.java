package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.exceptions.UserExistsException;
import com.test.skblab.models.UserRequestData;
import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Alexander Zubkov
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserApprovalService userApprovalService;

    @InjectMocks
    private RegistrationService registrationService;

    private final UserRequestData userRequestData;
    private final User user;

    public RegistrationServiceTest() {
        userRequestData = new UserRequestData();
        user = new User();
    }

    @Test(expected = NullPointerException.class)
    public void registration() throws UserExistsException {
        registrationService.userRegistration(any());
    }

    @Test(expected = UserExistsException.class)
    public void registrationUserExists() throws UserExistsException {
        when(userService.addUser(any())).thenThrow(new UserExistsException(""));
        registrationService.userRegistration(any());
    }

    @Test(expected = HibernateException.class)
    public void registrationDatabaseException() throws UserExistsException {
        when(userService.addUser(any())).thenThrow(new HibernateException(""));
        registrationService.userRegistration(any());
    }

    @Test(expected = RuntimeException.class)
    public void registrationSentApprovalException() throws UserExistsException {
        doThrow(new RuntimeException("")).when(userApprovalService).requestApproval(any());
        registrationService.userRegistration(any());
    }

    @Test
    public void registrationCheckRequestData() throws UserExistsException {
        when(userService.addUser(userRequestData)).thenReturn(user);
        registrationService.userRegistration(userRequestData);
        verify(userService, only())
                .addUser(userRequestData);
    }

    @Test
    public void registrationCheckRequestDataUser() throws UserExistsException {
        when(userService.addUser(userRequestData)).thenReturn(user);
        registrationService.userRegistration(userRequestData);
        verify(userApprovalService, only())
                .requestApproval(user);
    }

}