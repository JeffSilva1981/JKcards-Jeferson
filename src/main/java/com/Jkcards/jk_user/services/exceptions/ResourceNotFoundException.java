package com.Jkcards.jk_user.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String msn){
        super(msn);
    }
}
