package com.mytask.front.service.api;


import com.mytask.front.exception.LoginException;
import com.mytask.front.model.LabelModel;
import com.mytask.front.model.User;

import java.util.List;

public interface AuthApiClientInterface {
    void register(User user);
    boolean login(User user) throws LoginException;
}