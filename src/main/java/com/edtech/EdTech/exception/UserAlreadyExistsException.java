package com.edtech.EdTech.exception;

import com.edtech.EdTech.model.user.User;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message){
        super(message);
    }

}
