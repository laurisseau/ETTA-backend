package com.etta.edtech.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class Compiler {
    private String clientId;
    private String clientSecret;
    private String versionIndex;
    private String language;
    private String code;
}
