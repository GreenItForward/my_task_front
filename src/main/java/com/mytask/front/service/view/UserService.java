package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.model.Task;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.RoleApiClient;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.enums.ERole;
import com.mytask.front.utils.enums.EString;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.json.JSONException;

import java.util.List;
import java.util.Optional;
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
                try {
                    RoleApiClient.getInstance().excludeUser(user.getId(), ShowTabController.getInstance().getProject().getId());
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);
                }
                onDelete.accept(userInfo);
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

    protected static HBox createAssignedUserInfo(Task task, Consumer<HBox> onDelete) {
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

        userComboBox.setValue(task.getAssignedTo());

        userComboBox.setOnAction(e -> {
            User selectedUser = userComboBox.getValue();
            List<Task> tasks = ShowTabController.getInstance().getProject().getTasks();

            Optional<Task> taskToUpdate = tasks.stream()
                    .filter(t -> t.getId() == task.getId())
                    .findFirst();

            if (taskToUpdate.isPresent()) {
                if (selectedUser.getPrenom().isEmpty()) {
                    TaskApiClient.getInstance().updateTask(task);
                    taskToUpdate.get().setAssignedTo("");
                } else {
                    TaskApiClient.getInstance().updateTask(task, selectedUser.getId());
                    taskToUpdate.get().setAssignedTo(String.valueOf(selectedUser.getId()));
                }

                ShowTabController.getInstance().setCurrentTask(task);


                ShowTabController.getInstance().getProject().deleteTask(task);

                try {
                    ShowTabController.getInstance().resetController();
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                ShowTabController.getInstance().refreshTasks();
            } else {
                // TODO: Gérer le cas où la tâche n'est pas trouvée
            }
        });


        Button deleteButton = new Button("Supprimer");
        HBox userInfo = new HBox(10);


        deleteButton.setOnAction(e -> onDelete.accept(userInfo));

        userInfo.getChildren().addAll(userComboBox, deleteButton);

        return userInfo;
    }

}
