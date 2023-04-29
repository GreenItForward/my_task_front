package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EStatus;
import javafx.stage.Window;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProjectTabService {
    private static ProjectTabService instance;
    private final ScreenService screenService;

    private ProjectTabService() {
        screenService = ScreenService.getInstance(null);
    }

    public static ProjectTabService getInstance() {
        if (instance == null) {
            instance = new ProjectTabService();
        }
        return instance;
    }

    public void openProject(Project project) {
        System.out.println("Ouverture du projet " + project.getNom());
        screenService = ScreenService.getInstance(null);
        try {
            Project.setTasks(TaskApiClient.getInstance().getTasksByProject(project));
        } catch (JSONException | AuthException e) {
            throw new RuntimeException(e);
        }

        ShowTabController.getInstance().setProject(project);
        screenService.setScreen(EPage.SHOW_TAB);
    }

    public void closeCurrentPopup(Window window) {
       PopupService.getInstance().closeCurrentPopup(window);
    }

    protected VBox createProjectContent(Project project) {
        VBox projectContainer = new VBox();
        projectContainer.setSpacing(10);
        projectContainer.setStyle("-fx-padding: 10;");

        TextField nameField = new TextField(project.getNom());
        nameField.setPromptText(EString.NAME.toString());

        TextArea descriptionField = new TextArea(project.getDescription());
        descriptionField.setPromptText(EString.DESCRIPTION.toString());

        Button editLabelsButton = new Button(EString.EDIT_LABELS.toString());
        editLabelsButton.getStyleClass().add("button-edit-labels");
        Button saveButton = new Button(EString.SAVE.toString());
        saveButton.getStyleClass().add("button-save");

        saveButton.setOnAction(e -> {
            project.setNom(nameField.getText());
            project.setDescription(descriptionField.getText());
            // ProjectService.getInstance().updateProject(project);
        });

        editLabelsButton.setOnAction(e -> PopupService.getInstance().showEditLabelPopup((Stage) editLabelsButton.getScene().getWindow()));


        projectContainer.getChildren().addAll(nameField, descriptionField, editLabelsButton, saveButton);

        return projectContainer;
    }

    protected VBox createInviteCodeContent() {
        VBox inviteCodeContainer = new VBox();
        HBox inviteCodeBox = new HBox(10);
        HBox buttonBox = new HBox(10);
        inviteCodeContainer.setSpacing(10);
        inviteCodeContainer.setStyle("-fx-padding: 10;");
        Label inviteCodeLabel = new Label(EString.INVITE_CODE.toString());
        Label inviteLabel = new Label("");
        Button generateInviteCodeButton = new Button(EString.GENERATE_INVITE_CODE.toString());
        generateInviteCodeButton.setOnAction(e -> inviteLabel.setText(AppUtils.generateRandomInviteCode()));

        Button copyInviteCodeButton = new Button(EString.COPY_INVITE_CODE.toString());
        copyInviteCodeButton.setOnAction(e -> AppUtils.copyToClipboard(inviteLabel));

        buttonBox.getChildren().addAll(generateInviteCodeButton, copyInviteCodeButton);
        inviteCodeBox.getChildren().addAll(inviteCodeLabel, inviteLabel);
        inviteCodeContainer.getChildren().addAll(inviteCodeBox, buttonBox);

        return inviteCodeContainer;
    }

    protected VBox createMemberContent() {
        VBox userContainer = new VBox();
        userContainer.setSpacing(10);
        userContainer.setStyle("-fx-padding: 10;");

        // Exemple de données utilisateur (on le récupèrera de l'api)
        List<String[]> users = Arrays.asList(
                new String[]{"Ronan (vous)", "ronan@gmail.com", "Administrateur"},
                new String[]{"John", "johndoe@gmail.com", "Membre"}
        );

        Consumer<HBox> onDelete = userInfo -> {
            ButtonType result = AlertService.showAlertConfirmation(AlertService.EAlertType.CONFIRMATION, EString.DELETE_USER_TITLE.toString(), EString.DELETE_USER_CONFIRMATION.toString());
            if (AlertService.isConfirmed(result)) {
                userContainer.getChildren().remove(userInfo);
            }
        };

        users.forEach(user -> userContainer.getChildren().add(UserService.createUserInfo(user, onDelete)));

        return userContainer;
    }



}