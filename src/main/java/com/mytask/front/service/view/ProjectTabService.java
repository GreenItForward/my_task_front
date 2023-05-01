package com.mytask.front.service.view;

import com.mytask.front.controller.ShowAllTabController;
import com.mytask.front.controller.ShowTabController;
import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.ProjectApiClient;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.AppUtils;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EStatus;
import com.mytask.front.utils.EString;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.mytask.front.service.AppService.listenerDisableButton;

public class ProjectTabService {
    private static ProjectTabService instance;
    private ScreenService screenService;

    private ProjectTabService() {
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


        if (Project.getTasks() != null) {
            Project.getTasks().clear();
            LabelService.getInstance().resetAllLabels();
        }

        try {
            Project.setTasks(TaskApiClient.getInstance().getTasksByProject(project));
        } catch (JSONException | AuthException e) {
            throw new RuntimeException(e);
        }

        ShowTabController showTabController = ShowTabController.getInstance();


        VBox todoTasksList = showTabController.getTodoTasksList();
        VBox inProgressTasksList = showTabController.getInProgressTasksList();
        VBox doneTasksList = showTabController.getDoneTasksList();

        TabService.resetTab(todoTasksList, inProgressTasksList, doneTasksList);

        if (todoTasksList != null && inProgressTasksList != null && doneTasksList != null) {
            showTabController.setProject(project, todoTasksList, inProgressTasksList, doneTasksList);
            screenService.setScreen(EPage.SHOW_TAB);
        } else {
            System.out.println("Erreur lors de l'ouverture du projet");
        }
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
            ProjectApiClient.getInstance().updateProject(project);

            ShowAllTabController.getInstance().updateProjectList();
            try {
                ShowAllTabController.getInstance().setProjects(ProjectApiClient.getInstance().getProjectByUser());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        });

        listenerDisableButton(nameField, saveButton);
        listenerDisableButton(descriptionField, saveButton);

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


    public void setProject(Project project) {
        ShowTabController showTabController = ShowTabController.getInstance();
        showTabController.setProject(project);
    }

    public VBox createExportContent(Project project) {
        VBox exportContainer = new VBox();
        exportContainer.setSpacing(10);
        exportContainer.setStyle("-fx-padding: 10;");

        Button exportPdfButton = new Button(EString.EXPORT_TO_PDF.toString());
        exportPdfButton.getStyleClass().add("button-export-pdf");

        exportPdfButton.setOnAction(e -> TaskApiClient.getInstance().exportTasksToPdf(project));

        Button exportCsvButton = new Button(EString.EXPORT_TO_CSV.toString());
        exportCsvButton.getStyleClass().add("button-export-pdf");

        exportCsvButton.setOnAction(e -> TaskApiClient.getInstance().exportTasksToCsv(project));

        exportContainer.getChildren().addAll(exportPdfButton, exportCsvButton);

        return exportContainer;
    }
}