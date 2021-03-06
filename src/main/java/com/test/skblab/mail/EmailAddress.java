package com.test.skblab.mail;

/**
 * @author Alexander Zubkov
 */
public class EmailAddress {

    private String email;

    public EmailAddress(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return email;
    }
}
