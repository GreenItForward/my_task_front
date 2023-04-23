package com.mytask.front.utils;

public enum EString {
    // Buttons
    BACK("Retour"),

    CLOSE("Fermer"),
    BACK_TO_MENU("Retour au menu"),
    CREATE_TAB("Créer un tableau"),
    JOIN_TAB("Rejoindre un tableau"),
    CONNECTION("Connexion"),
    SIGN_UP("Inscription"),
    VIEW_MEMBERS("Voir les membres"),
    COPY_INVITE_CODE("Copier le code d'invitation"),
    ADD_TASK("Ajouter une tâche"),
    ADD_LABEL("Ajouter une étiquette"),

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
    EDIT_USER_ROLE("Modifier les droits ou supprimer les utilisateurs"),
    INVITE_CODE("Code d'invitation : "),
    EXPORT_TO_PDF("Exporter en PDF"),

    // Name App
    NAME_APP("MyTask"),
    NAME_ORGANIZATION("GreenItForward"),

    // Errors messages
    EMPTY_FIELD("Veuillez remplir ce champ"),
    
    // Formatters
    DATE_FORMAT("yyyy-MM-dd"),


    // Roles and actions
    ADMINISTRATEUR("Administrateur"),
    MEMBRE("Membre"),
    SUPPRIMER("Supprimer"),

    // Alert messages
    DELETE_USER_TITLE("Supprimer un utilisateur"),
    ALERT_VERIFICATION("Etes-vous sûr de vouloir continuer ?"),
    DELETE_USER_CONFIRMATION("Voulez-vous vraiment supprimer cet utilisateur ?"),
    //popup
    INVITE_CODE_COPIED_TITLE("Code d'invitation copié"),
    INVITE_CODE_COPIED_MESSAGE("Le code d'invitation a été copié dans le presse-papier"),

    // API
    API_URI("http://localhost:3000/"),

    // Messages LOGS
    SIGN_IN_IN_PROGRESS("Connexion en cours..."),
    SIGN_UP_IN_PROGRESS("Inscription en cours..."),
    SHOW_TAB_LOG("Voir tableau"),
    CREATE_TAB_LOG("Créer tableau"),
    SHOW_TABLES("Afficher les tableaux");

    private String string;

    EString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static String[] getRoleStrings() {
        return new String[]{
                ADMINISTRATEUR.toString(),
                MEMBRE.toString(),
                SUPPRIMER.toString()
        };
    }

    public static String getCompleteNameApp() {
        return NAME_APP + " - " + NAME_ORGANIZATION;
    }

    public static String getNameTimestamp() {
        return NAME_APP + "-" + System.currentTimeMillis();
    }

}
