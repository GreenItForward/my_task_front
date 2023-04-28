package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.controller.TaskDetailsController;
import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.mytask.front.utils.EPopup.LABELS;
import static com.mytask.front.utils.EPopup.TASK_DETAILS;

public class PopupService {
    private static TaskApiClient taskApiClient;
    private static PopupService instance;
    private final ProjectTabService projectTabService;

    private PopupService() {
        projectTabService = ProjectTabService.getInstance();

    }

    public static PopupService getInstance() {
        if (instance == null) {
            instance = new PopupService();
        }
        return instance;
    }

    public static void setPopupScreen(Stage primaryStage, EPopup page, VBox content) {
        setPopupScreen(primaryStage, page, content, null);
    }

    public static void setPopupScreen(Stage primaryStage, EPopup page, VBox content, Supplier<Task> taskSupplier) {
        taskApiClient = TaskApiClient.getInstance();
        Stage popup = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");

        popup.initModality(Modality.WINDOW_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(page.getWindowTitle());

        Label label = new Label(page.getWindowTitle());
        label.getStyleClass().add("popup-title");
        Button closeButton = new Button(EString.CLOSE.toString());

        closeButton.setOnAction(e -> {
            if (taskSupplier != null) {
                updateInformation(page, taskSupplier);
            }
            popup.close();
        });

        if (taskSupplier != null) {
            popup.setOnCloseRequest(e -> updateInformation(page, taskSupplier));
        }

        scrollPane.setContent(content);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, scrollPane, closeButton);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene popupScene = new Scene(layout, page.getWidth(), page.getHeight());
        popupScene.getStylesheets().add(Objects.requireNonNull(PopupService.class.getResource(EStyle.STYLES.getCssPath())).toExternalForm());
        popupScene.getStylesheets().add(Objects.requireNonNull(PopupService.class.getResource(EStyle.POPUP.getCssPath())).toExternalForm());
        
        popup.setScene(popupScene);
        popup.getIcons().add(new Image(Objects.requireNonNull(PopupService.class.getResourceAsStream(EIcon.GIF.getImagePath()))));
        popup.centerOnScreen();
        popup.setResizable(false);
        popup.showAndWait();
    }

    private static void updateInformation(EPopup page, Supplier<Task> taskSupplier) {
        if (page.getFxmlName().equals(TASK_DETAILS.getFxmlName())) {
            Task task = taskSupplier.get();
            if (task != null) {
                taskApiClient.updateTask(task);
            }
        }else if(page.getFxmlName().equals(LABELS.getFxmlName())){
            Task task = taskSupplier.get();
            ShowTabController.getInstance().updateLabels(task);
            // TaskDetailsController.getInstance().updateAssignedMembers();
        }
    }

    public void showMemberPopup(Stage primaryStage) {
        VBox userContainer = projectTabService.createMemberContent();
        setPopupScreen(primaryStage, EPopup.MEMBERS, userContainer);
    }

    public static void showProjectSettingsPopup(Stage window, Project project) {
        VBox projectContainer = ProjectTabService.getInstance().createProjectContent(project);
        setPopupScreen(window, EPopup.PROJECT_SETTINGS, projectContainer);
    }

    public void showEditLabelPopup(Stage primaryStage) {
        VBox labelContainer = LabelService.getInstance().createEditLabelContent();
        setPopupScreen(primaryStage, LABELS, labelContainer);
    }

    public void showLabelPopup(Stage primaryStage, Task task) {
        VBox labelContainer = LabelService.getInstance().createLabelContent(task);
        setPopupScreen(primaryStage, LABELS, labelContainer, () -> task);
    }

    protected void updateToggleButton(CheckBox checkBox, Task task, LabelModel label) {
        if (task.getLabels().contains(label)) {
            checkBox.getStyleClass().remove("checkbox-add");
        } else {
            checkBox.getStyleClass().remove("checkbox-delete");
        }
    }

    public static void showTaskDetailPopup(Stage primaryStage, Task task) {
        try {
            URL fxmlUrl = EPopup.TASK_DETAILS.getFxmlPath();
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent taskDetailContent = loader.load();
            TaskDetailsController taskDetailController = loader.getController();
            taskDetailController.setTaskAndUpdateUI(task);
            setPopupScreen(primaryStage, EPopup.TASK_DETAILS, new VBox(taskDetailContent), taskDetailController::getTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void showTablesPopup() {
        ListView<String> tablesPopupListView = new ListView<>();
        List<Project> projects =  ShowAllTabService.getInstance().getProjects();
        projects.forEach(project -> tablesPopupListView.getItems().add(project.getNom()));
        Button openTableButton = new Button(EString.OPEN_TABLE.toString());
        openTableButton.setOnAction(e -> {
            String selectedTable = tablesPopupListView.getSelectionModel().getSelectedItem();
            if (selectedTable != null) {
                Project project = projects.stream().filter(p -> p.getNom().equals(selectedTable)).findFirst().orElse(null);
                if (project != null) {
                    ProjectTabService projectTabService = ProjectTabService.getInstance();
                    projectTabService.closeCurrentPopup(openTableButton.getScene().getWindow());
                    projectTabService.openProject(project);
                }
            }

        });

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(EString.MY_TABS.toString());

        VBox popupVBox = new VBox(10);
        popupVBox.getChildren().addAll(new Label(EString.MY_TABS.toString()), tablesPopupListView, openTableButton);
        popupVBox.setPadding(new Insets(10, 10, 10, 10));

        Scene popupScene = new Scene(popupVBox, EPopup.TABLE_LIST.getWidth(), EPopup.TABLE_LIST.getHeight());
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    public void showInviteCodePopup(Stage primaryStage) {
        VBox inviteCodeContainer = projectTabService.createInviteCodeContent();
        setPopupScreen(primaryStage, EPopup.INVITE_CODE, inviteCodeContainer);
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PopupService.class.getResource(fxmlPath));
        return fxmlLoader.load();
    }


    public void closeCurrentPopup(Window window) {
        if (window != null) {
            window.hide();
        }
    }
}
