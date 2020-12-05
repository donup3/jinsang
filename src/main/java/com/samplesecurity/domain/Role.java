package com.samplesecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER"),
    LAWYER("ROLE_LAWYER"),
    CS("ROLE_CS"),
    CONSULTANT("ROLE_CONSULTANT");

    private String value;
}
