package com.mytask.front.service.api;


import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;

public interface AuthApiClientInterface {
    String authentify(User user, String endpoint) throws AuthException;
    User getUser(String token) throws AuthException;
}