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

    private HttpStatus httpStatus;
    private Date date;
    private String message;
    private StackTraceElement[] lineError;

    public ErrorMessage(){
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "\n httpStatus= " + httpStatus +
                ",\n date= " + date +
                ",\n message= '" + message + '\'' +
                ",\n lineError= " + Arrays.toString(lineError) +
                '}';
    }

}
