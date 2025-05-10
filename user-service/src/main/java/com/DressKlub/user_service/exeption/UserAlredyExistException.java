package com.DressKlub.user_service.exeption;

public class UserAlredyExistException  extends RuntimeException{
    public UserAlredyExistException(String message){
        super(message);
    }
}
