package com.goldenengineering.coffeebara.common.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    MANAGER("관리자"), MASTER("마스터");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    String getValue(){
        return value;
    }

    public static Role from(String role){
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role" + role));
    }
}
