package com.example.sufya.draft.models;

/**
 * @Author Ajmal, R. (2016).
 * @Website https://www.learn2crack.com/2016/04/android-login-registration-php-mysql-client.html
 */

public class ServerRequest {

    private String operation;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
