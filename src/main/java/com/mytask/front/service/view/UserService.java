package com.mytask.front.service.view;

import com.mytask.front.model.User;
import java.util.ArrayList;

public class UserService {
    private static User currentUser = new User();

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        UserService.currentUser = currentUser;
    }
}
