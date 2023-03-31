package com.mytask.front.utils;

public enum EString {
    // Buttons
    BACK("Retour"),
    BACK_TO_MENU("Retour au menu"),
    CREATE_TAB("Créer un tableau"),
    JOIN_TAB("Rejoindre un tableau"),
    CONNECTION("Connexion"),
    SIGN_UP("Inscription"),
    VIEW_MEMBERS("Voir les membres"),
    // Labels
    MY_TABS("Mes tableaux"),
    LABEL_JOIN_TAB("Entrez le code du tableau que vous souhaitez rejoindre :"),
    NAME_TAB("Nom du tableau"),
    DESCRIPTION_TAB("Description du tableau"),
    SELECT_COLOR("Sélection de couleur"),
    NAME("Nom"),
    DESCRIPTION("Description"),
    LABEL("Etiquette"),
    GENERATE_INVITE_CODE("Générer un code d'invitation"),
    TODO("TODO"),
    IN_PROGRESS("IN PROGRESS"),
    DONE("DONE"),
    WELCOME("Bienvenue "),
    INFORMATION_TAB("Informations du tableau"),
    OPEN_TABLE("Ouvrir le tableau"),

    // Messages LOGS
    SIGN_IN_IN_PROGRESS("Connexion en cours..."),
    SIGN_UP_IN_PROGRESS("Inscription en cours..."),
    SHOW_TAB_LOG("Voir tableau"),
    CREATE_TAB_LOG("Créer tableau");


    private String string;

    EString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }



}
