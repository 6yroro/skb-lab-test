package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.exceptions.UserExistsException;
import com.test.skblab.models.UserRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alexander Zubkov
 */
@Service
public class RegistrationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final UserApprovalService userApprovalService;

    @Autowired
    public RegistrationService(UserService userService, UserApprovalService userApprovalService) {
        this.userService = userService;
        this.userApprovalService = userApprovalService;
    }

    /*
    добавление пользователя и отправка запроса на одобрение атомарная операция
    можно вынести отправку за транзакцию, но тогда нужно предусмотреть ситуацию, когда отправка сломалась,
    а в базе остались данные - можно, как пример, обойти всех у кого approval null и отправить запрос повторно
     */
    @Transactional
    public void userRegistration(UserRequestData userRequestData) throws UserExistsException {
        User user = userService.addUser(userRequestData);
        userApprovalService.requestApproval(user);
        log.info("User " + user.getLogin() + " is saved");
    }

}
