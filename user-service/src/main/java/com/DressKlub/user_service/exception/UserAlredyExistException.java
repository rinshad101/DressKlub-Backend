package com.DressKlub.user_service.exception;

public class UserAlredyExistException extends RuntimeException{
    public UserAlredyExistException(String message){
        super(message);
    }
}
