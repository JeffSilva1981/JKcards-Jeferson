package com.Jkcards.jk_user.services.exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String msn){
        super(msn);
    }
}
