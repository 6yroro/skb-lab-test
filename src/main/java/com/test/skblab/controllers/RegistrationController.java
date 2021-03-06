package com.test.skblab.controllers;

import com.test.skblab.exceptions.UserExistsException;
import com.test.skblab.models.UserRequestData;
import com.test.skblab.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Alexander Zubkov
 */
@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity registration(@Valid @RequestBody UserRequestData userRequestData) throws UserExistsException {
        registrationService.userRegistration(userRequestData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
