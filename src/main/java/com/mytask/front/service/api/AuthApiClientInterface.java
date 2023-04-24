package com.mytask.front.service.api;


import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;

public interface AuthApiClientInterface {
    void authentify(User user, String endpoint) throws AuthException;
}