package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.RoleApiClient;
import com.mytask.front.utils.enums.ERole;
import com.mytask.front.utils.enums.EString;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.json.JSONException;

import java.util.List;
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

    protected static HBox createAssignedUserInfo(User user, Consumer<HBox> onDelete) {
        List<User> allUsers = ShowTabController.getInstance().getAllUsers();

        ComboBox<User> userComboBox = new ComboBox<>();
        allUsers.add(new User());
        userComboBox.getItems().addAll(allUsers);
        userComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user.getPrenom();
            }

            @Override
            public User fromString(String string) {
                return userComboBox.getItems().stream()
                        .filter(user -> user.getPrenom().equals(string))
                        .findFirst().orElse(null);
            }
        });

        userComboBox.setValue(user);

        userComboBox.setOnAction(e -> {
            User selectedUser = userComboBox.getValue();
            System.out.println("Selected User: " + selectedUser.getPrenom());
            // TODO :  met à jour l'utilisateur assigné à la tâche
            if (selectedUser.getPrenom().isEmpty()) {
                System.out.println("Selected User is null");
                // TODO :  supprime l'utilisateur assigné à la tâche
            } else {
                System.out.println("Selected User is not null");
                // TODO :  met à jour l'utilisateur assigné à la tâche
            }

        });

        Button deleteButton = new Button("Supprimer");
        HBox userInfo = new HBox(10);


        deleteButton.setOnAction(e -> onDelete.accept(userInfo));

        userInfo.getChildren().addAll(userComboBox, deleteButton);

        return userInfo;
    }

}
