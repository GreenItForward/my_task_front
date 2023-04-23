package com.mytask.front.service.view;

import com.mytask.front.exception.LoginException;
import com.mytask.front.exception.SignupException;
import com.mytask.front.model.User;

import java.util.ArrayList;

public class UserService {
    private static User currentUser = new User();
    private static final ArrayList<User> allUsers = new ArrayList<>();

    static {
        allUsers.add(new User("admin", "admin", "admin", "admin"));
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    /** Connecte l'utilisateur si il existe dans la liste des utilisateurs.
     * Renvoie une booléen indiquant si la connection a été réalisé avec succès */
    public static Boolean connectUser(String email, String password) throws LoginException {
        if(email.isBlank() || password.isBlank()) {
            throw new LoginException("Certains champs sont vides");
        }
        return true;
    }

    /** Inscrit l'utilisateur si son email n'est pas déjà utilisé.
     * Renvoie une booléen indiquant si l'inscription a été réalisé avec succès */
    public static Boolean signUpUser(User user) throws SignupException {
        if(user.getEmail().isBlank() || user.getNom().isBlank() || user.getPrenom().isBlank() || user.getPassword().isBlank()) {
            throw new SignupException("Certains champs sont vides");
        }

        for(User u : allUsers) {
            if(u.getEmail().equals(user.getEmail())) {
                throw new SignupException("Cet email est déjà utilisé");
            }
        }
        allUsers.add(user);
        UserService.currentUser = user;
        return true;
    }
}
