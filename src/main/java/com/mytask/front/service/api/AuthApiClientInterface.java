package com.mytask.front.service.api;


import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.utils.enums.EAuthEndpoint;

public interface AuthApiClientInterface {
    String authentify(User user, EAuthEndpoint endpoint) throws AuthException;
    User getUser(String token) throws AuthException;
    User getUserById(int id) throws AuthException;
}