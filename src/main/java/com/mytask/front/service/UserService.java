package com.mytask.front.service;

import com.mytask.front.utils.User;

import java.util.ArrayList;

public class UserService {
    private User currentUser;
    private ArrayList<User> allUsers;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
