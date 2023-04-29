package com.mytask.front.service.view;

import com.mytask.front.model.User;
import com.mytask.front.utils.EString;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class UserService {
    private static UserService instance;
    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private static User currentUser = new User();

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        UserService.currentUser = currentUser;
    }


    protected static HBox createUserInfo(String[] user, Consumer<HBox> onDelete) {
        Label nameLabel = new Label(user[0]);
        Label emailLabel = new Label(user[1]);

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(EString.getRoleStrings());
        roleComboBox.setValue(user[2]);

        HBox userInfo = new HBox(10);
        userInfo.getChildren().addAll(nameLabel, emailLabel, roleComboBox);

        roleComboBox.setOnAction(e -> {
            if (roleComboBox.getValue().equals(EString.SUPPRIMER.toString())) {
                onDelete.accept(userInfo);
            } else {
                roleComboBox.setValue(user[2]);
            }
        });

        roleComboBox.getStyleClass().add("combo-box");

        return userInfo;
    }
}
