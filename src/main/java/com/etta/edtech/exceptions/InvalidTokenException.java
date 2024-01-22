package com.etta.edtech.exceptions;

import com.etta.edtech.model.ErrorMessage;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(
            String message) {
        super(message);
    }
}
