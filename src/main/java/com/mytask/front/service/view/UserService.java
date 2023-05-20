package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.RoleApiClient;
import com.mytask.front.utils.enums.ERole;
import com.mytask.front.utils.enums.EString;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.json.JSONException;

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


    protected static HBox createUserInfo(User user, Consumer<HBox> onDelete) {
        Label nameLabel = new Label(user.getPrenom());
        Label emailLabel = new Label(user.getEmail());

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(EString.getRoleStrings());
        roleComboBox.setValue(user.getRole());

        HBox userInfo = new HBox(10);
        userInfo.getChildren().addAll(nameLabel, emailLabel, roleComboBox);

        if(!UserService.getCurrentUser().getRole().equals(ERole.ADMINISTRATEUR.getValue()) || UserService.getCurrentUser().getId() == user.getId()) {
            roleComboBox.setDisable(true);
        }

        roleComboBox.setOnAction(e -> {
            if (roleComboBox.getValue().equals(EString.SUPPRIMER.toString())) {
                onDelete.accept(userInfo);
                // TODO
            } else {
                try {
                    RoleApiClient.getInstance().changeRole(user.getId(), ShowTabController.getInstance().getProject().getId(),
                            ERole.findByName(roleComboBox.getValue()).getValue());
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                user.setRole(ERole.valueOf(ERole.findByName(roleComboBox.getValue()).getValue()));

                roleComboBox.setValue(user.getRole());
            }
        });

        roleComboBox.getStyleClass().add("combo-box");

        return userInfo;
    }
}
