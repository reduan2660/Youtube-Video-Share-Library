package com.ytvideoshare.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalValue {

    public static String jwtsecret;

    @Value("${JWTSECRET}")
    public void setJwtsecret(String db) {
        jwtsecret = db;
    }
}
