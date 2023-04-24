package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.controller.TaskDetailsController;
import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import com.mytask.front.service.AppService;
import com.mytask.front.service.api.impl.LabelApiClient;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mytask.front.utils.EPopup.LABELS;
import static com.mytask.front.utils.EPopup.TASK_DETAILS;

public class PopupService {
    private static TaskApiClient taskApiClient;
    private PopupService instance;


    private PopupService() {

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
            updateInformation(page, taskSupplier);
            popup.close();
        });

        popup.setOnCloseRequest(e -> updateInformation(page, taskSupplier));

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

    public static void showMemberPopup(Stage primaryStage) {
        VBox userContainer = createMemberContent();
        setPopupScreen(primaryStage, EPopup.MEMBERS, userContainer);
    }

    public static PopupService getInstance() {
        return new PopupService();
    }

    public void showLabelPopup(Stage primaryStage, Task task) {
        VBox labelContainer = this.createLabelContent(task);
        setPopupScreen(primaryStage, LABELS, labelContainer, () -> task);
    }

    private static HBox createLabelInfo(String[] label, Consumer<HBox> onDelete) {
        TextField nameLabel = new TextField(label[0]);
        ColorPicker colorPicker = new ColorPicker(Color.web(label[1]));

        Button deleteButton = new Button(EString.SUPPRIMER.toString());
        deleteButton.getStyleClass().add("button-delete");

        HBox labelInfo = new HBox(10);
        labelInfo.getChildren().addAll(nameLabel, colorPicker, deleteButton);

        colorPicker.setOnAction(e -> {
            Color newColor = colorPicker.getValue();
            String colorString = AppService.colorToHexString(newColor); // Pour l'api
        });

        return labelInfo;
    }

    private VBox createLabelContent(Task task) {
        VBox labelContainer = new VBox();
        labelContainer.setSpacing(10);
        labelContainer.setStyle("-fx-padding: 10;");

        // Exemple de données label
        List<LabelModel> labels = ShowTabController.getInstance().getProject().getLabels();

        for (LabelModel label : labels) {
            TextField nameLabel = new TextField(label.getNom());
            ColorPicker colorPicker = new ColorPicker(label.getCouleur());

            Button toggleButton = new Button();
            updateToggleButton(toggleButton, task, label);

            HBox labelInfo = new HBox(10);
            labelInfo.getChildren().addAll(nameLabel, colorPicker, toggleButton);
            labelContainer.getChildren().add(labelInfo);

            toggleButton.setOnAction(e -> {
                toggleLabel(toggleButton, task, label);
            });

            nameLabel.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    label.setNom(newValue);
                }
            });

            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    label.setCouleur(newValue);
                }
            });

            // Update label color when ColorPicker value changes
            colorPicker.setOnAction(e -> {
                Color newColor = colorPicker.getValue();
                String colorString = AppService.colorToHexString(newColor); // Pour l'api
            });
        }

        return labelContainer;
    }


    private static void updateToggleButton(Button toggleButton, Task task, LabelModel label) {
        if (task.getLabels().contains(label)) {
            toggleButton.setText(EString.SUPPRIMER.toString());
            toggleButton.getStyleClass().remove("button-add");
            toggleButton.getStyleClass().add("button-delete");
        } else {
            toggleButton.setText(EString.AJOUTER.toString());
            toggleButton.getStyleClass().remove("button-delete");
            toggleButton.getStyleClass().add("button-add");
        }
    }

    private static void toggleLabel(Button toggleButton, Task task, LabelModel label) {
        if (task.getLabels().contains(label)) {
            task.getLabels().remove(label);
            LabelApiClient.getInstance().removeLabel(label);
        } else {
            task.getLabels().add(label);
            LabelApiClient.getInstance().addLabel(label);
        }
        updateToggleButton(toggleButton, task, label);
    }

    private static HBox createUserInfo(String[] user, Consumer<HBox> onDelete) {
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

    private static VBox createMemberContent() {
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

        users.forEach(user -> userContainer.getChildren().add(createUserInfo(user, onDelete)));

        return userContainer;
    }

    private static VBox createInviteCodeContent() {
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

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(EString.MY_TABS.toString());

        VBox popupVBox = new VBox(10);
        popupVBox.getChildren().addAll(new Label(EString.MY_TABS.toString()), tablesPopupListView);
        popupVBox.setPadding(new Insets(10, 10, 10, 10));

        Scene popupScene = new Scene(popupVBox, EPopup.TABLE_LIST.getWidth(), EPopup.TABLE_LIST.getHeight());
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    public static void showInviteCodePopup(Stage primaryStage) {
        VBox inviteCodeContainer = createInviteCodeContent();

        setPopupScreen(primaryStage, EPopup.INVITE_CODE, inviteCodeContainer);
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PopupService.class.getResource(fxmlPath));
        return fxmlLoader.load();
    }


}
