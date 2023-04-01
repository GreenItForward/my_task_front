package com.mytask.front.service;

import com.mytask.front.utils.User;

import java.util.ArrayList;

public class UserService {
    private static User currentUser;
    private static ArrayList<User> allUsers;

    private UserService() {
        currentUser = new User();
        allUsers = new ArrayList<>();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    /** Connecte l'utilisateur si il existe dans la liste des utilisateurs.
     * Renvoie une booléen indiquant si la connection a été réalisé avec succès */
    public static boolean connectUser(String email, String password) {
        for(User u : allUsers) {
            if(u.getEmail().equals(email)) {
                if(u.getPassword().equals(password)) {
                    UserService.currentUser = u;
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }

    /** Inscrit l'utilisateur si son email n'est pas déjà utilisé.
     * Renvoie une booléen indiquant si l'inscription a été réalisé avec succès */
    public static boolean signUpUser(User user) {
        for(User u : allUsers) {
            if(u.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        allUsers.add(user);
        UserService.currentUser = user;
        return true;
    }
}
