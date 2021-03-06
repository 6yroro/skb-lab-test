package com.test.skblab.mail;

/**
 * @author Alexander Zubkov
 */
public class EmailContent {

    private String content;

    public EmailContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

}
