package com.mytask.front.utils;

import com.mytask.front.service.view.AlertService;
import com.mytask.front.utils.enums.EString;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Random;

public class AppUtils {

    public static String generateRandomInviteCode() {
        // Le code est composé de (Min 2 Maj, Min 2 Num, Min 1 Caractère spécial et Min 8 caractères)
        StringBuilder inviteCode = new StringBuilder();

        // au moins 2 majuscules
        for (int i = 0; i < 2; i++) {
            char randomUpperCase = (char) ('A' + new Random().nextInt(26));
            inviteCode.append(randomUpperCase);
        }

        // Générer au moins 2 chiffres
        for (int i = 0; i < 2; i++) {
            char randomNumber = (char) ('0' + new Random().nextInt(10));
            inviteCode.append(randomNumber);
        }

        // Générer au moins 1 caractère spécial
        String specialCharacters = "!@#$%^&*()-_=+[{]};:'\",<.>/?\\|";
        char randomSpecialChar = specialCharacters.charAt(new Random().nextInt(specialCharacters.length()));
        inviteCode.append(randomSpecialChar);

        // Ajouter des caractères supplémentaires pour atteindre au moins 6 caractères
        int minLength = 8;
        while (inviteCode.length() < minLength) {
            int randomType = new Random().nextInt(3);
            char randomChar;

            if (randomType == 0) { // Majuscule
                randomChar = (char) ('A' + new Random().nextInt(26));
            } else if (randomType == 1) { // Chiffre
                randomChar = (char) ('0' + new Random().nextInt(10));
            } else { // Caractère spécial
                randomChar = specialCharacters.charAt(new Random().nextInt(specialCharacters.length()));
            }

            inviteCode.append(randomChar);
        }

        return inviteCode.toString();
    }

    public static void copyToClipboard(Label label) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(label.getText());
        clipboard.setContent(content);
        AlertService.showAlertInfo(EString.INVITE_CODE_COPIED_TITLE.toString(), EString.INVITE_CODE_COPIED_MESSAGE.toString());
    }
}


