package com.etta.edtech.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Date;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private Date date = new Date();
    private String message;
    private String exceptionName;
    private StackTraceElement[] lineError;

    public ErrorMessage(){
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "\n httpStatus= " + httpStatus +
                ",\n date= " + date +
                ",\n exceptionName= " + exceptionName +
                ",\n message= '" + message + '\'' +
                ",\n lineError= " + Arrays.toString(lineError) +
                '}';
    }

}
