package com.test.skblab.exceptions;

/**
 * @author Alexander Zubkov
 */
public class UserExistsException extends Exception {

    public UserExistsException(String msg) {
        super(msg);
    }

}
