package com.mytask.front.service;

import com.mytask.front.utils.User;

import java.util.ArrayList;

public class UserService {
    private static User currentUser = new User();
    private static final ArrayList<User> allUsers = new ArrayList<>();

    public static User getCurrentUser() {
        return currentUser;
    }

    /** Connecte l'utilisateur si il existe dans la liste des utilisateurs.
     * Renvoie une booléen indiquant si la connection a été réalisé avec succès */
    public static String connectUser(String email, String password) {
        if(email.equals("") || password.equals("")) {
            return "Certains champs sont vides";
        }

        for(User u : allUsers) {
            if(u.getEmail().equals(email)) {
                if(u.getPassword().equals(password)) {
                    UserService.currentUser = u;
                    return "ok";
                }
                else {
                    return "Le mot de passe est incorrect";
                }
            }
        }
        return "Il n'existe pas d'utilisateur avec cet adresse email";
    }

    /** Inscrit l'utilisateur si son email n'est pas déjà utilisé.
     * Renvoie une booléen indiquant si l'inscription a été réalisé avec succès */
    public static String signUpUser(User user) {
        if(user.getEmail().equals("") || user.getNom().equals("") || user.getPrenom().equals("") || user.getPassword().equals("")) {
            return "Certains champs sont vides";
        }

        for(User u : allUsers) {
            if(u.getEmail().equals(user.getEmail())) {
                return "Cet email est déjà utilisé";
            }
        }
        allUsers.add(user);
        UserService.currentUser = user;
        return "ok";

    }
}
