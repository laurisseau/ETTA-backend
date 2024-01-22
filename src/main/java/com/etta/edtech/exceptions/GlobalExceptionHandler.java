package com.etta.edtech.exceptions;

import com.amazonaws.services.accessanalyzer.model.ResourceNotFoundException;
import com.etta.edtech.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {

        StackTraceElement[] stackTraceElements = new StackTraceElement[]{ex.getStackTrace()[0]};
        String exceptionName = ex.getClass().getSimpleName();
        ErrorMessage errorMessage = new ErrorMessage();

        if (ex instanceof CognitoIdentityProviderException || ex instanceof ResourceNotFoundException) {
            handleSpecificException(ex, exceptionName, errorMessage, stackTraceElements);
        } else {
            handleGeneralException(ex, exceptionName, errorMessage, stackTraceElements);
        }

        System.out.println(errorMessage.toString() + "global");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    private void handleSpecificException(Exception ex, String exceptionName, ErrorMessage errorMessage, StackTraceElement[] stackTraceElements) {
        if (ex instanceof CognitoIdentityProviderException) {
            errorMessage.setMessage(((CognitoIdentityProviderException) ex).awsErrorDetails().errorMessage());
        } else if (ex instanceof ResourceNotFoundException) {
            errorMessage.setMessage(((ResourceNotFoundException) ex).getErrorMessage());
        }

        errorMessage.setExceptionName(exceptionName);
        errorMessage.setLineError(stackTraceElements);
    }

    private void handleGeneralException(Exception ex, String exceptionName, ErrorMessage errorMessage, StackTraceElement[] stackTraceElements) {
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setExceptionName(exceptionName);
        errorMessage.setLineError(stackTraceElements);
    }
}
