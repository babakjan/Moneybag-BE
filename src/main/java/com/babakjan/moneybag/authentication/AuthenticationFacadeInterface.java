package com.babakjan.moneybag.authentication;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {
    Authentication getAuthentication();
}
