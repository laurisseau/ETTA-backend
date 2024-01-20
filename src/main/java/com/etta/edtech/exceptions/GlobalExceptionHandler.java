package com.etta.edtech.exceptions;

import com.etta.edtech.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;


@ControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex)  {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Date date = new Date();
        StackTraceElement[] stackTraceElements = new StackTraceElement[]{ex.getStackTrace()[0]};
        ErrorMessage errorMessage = new ErrorMessage();

        errorMessage.setMessage(ex.getMessage());
        errorMessage.setDate(date);
        errorMessage.setHttpStatus(httpStatus);
        errorMessage.setLineError(stackTraceElements);

        System.out.println(errorMessage.toString());

        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
    }

}
