package com.mytask.front.service.view;

import com.mytask.front.utils.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PopupService {


    private PopupService() {
        throw new IllegalStateException("Utility class");
    }

    public static void setPopupScreen(Stage primaryStage, EPopup page, VBox content) {
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
        Button closeButton = new Button(EString.CLOSE.getString());
        closeButton.setOnAction(e -> popup.close());

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

    public static void showMemberPopup(Stage primaryStage) {
        VBox userContainer = createMemberContent();
        setPopupScreen(primaryStage, EPopup.MEMBERS, userContainer);
    }

    public static void showInviteCodePopup(Stage primaryStage) {
        VBox inviteCodeContainer = createInviteCodeContent();

        setPopupScreen(primaryStage, EPopup.INVITE_CODE, inviteCodeContainer);
    }

    private static VBox createMemberContent() {
        VBox userContainer = new VBox();
        userContainer.setSpacing(10);
        userContainer.setStyle("-fx-padding: 10;");

        // Exemple de données utilisateur
        List<String[]> users = Arrays.asList(
                new String[]{"Ronan (vous)", "ronan@gmail.com", "Administrateur"},
                new String[]{"John", "johndoe@gmail.com", "Membre"}
        );

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        for (int i = 0; i < users.size(); i++) {
            Label nameLabel = new Label(users.get(i)[0]);
            Label emailLabel = new Label(users.get(i)[1]);

            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.getItems().addAll(EString.getRoleStrings());
            roleComboBox.setValue(users.get(i)[2]);

            HBox userInfo = new HBox(10);
            userInfo.getChildren().addAll(nameLabel, emailLabel, roleComboBox);
            userContainer.getChildren().add(userInfo);

            int finalI = i;
            roleComboBox.setOnAction(e -> {
                if (roleComboBox.getValue().equals(EString.SUPPRIMER.getString())) {
                    ButtonType result = AlertService.showAlertConfirmation(AlertService.EAlertType.CONFIRMATION, EString.DELETE_USER_TITLE.getString(), EString.DELETE_USER_CONFIRMATION.getString());
                    if (AlertService.isConfirmed(result)) {
                        userContainer.getChildren().remove(userInfo);
                    } else {
                        roleComboBox.setValue(users.get(finalI)[2]);
                    }
                }
            });

            roleComboBox.getStyleClass().add("combo-box");
        }

        gridPane.add(userContainer, 0, 0, 3, 1);

        return userContainer;
    }

    private static VBox createInviteCodeContent() {
        VBox inviteCodeContainer = new VBox();
        HBox inviteCodeBox = new HBox(10);
        HBox buttonBox = new HBox(10);
        inviteCodeContainer.setSpacing(10);
        inviteCodeContainer.setStyle("-fx-padding: 10;");
        Label inviteCodeLabel = new Label(EString.INVITE_CODE.getString());
        Label inviteLabel = new Label("");
        Button generateInviteCodeButton = new Button(EString.GENERATE_INVITE_CODE.getString());
        generateInviteCodeButton.setOnAction(e -> inviteLabel.setText(AppUtils.generateRandomInviteCode()));

        Button copyInviteCodeButton = new Button(EString.COPY_INVITE_CODE.getString());
        copyInviteCodeButton.setOnAction(e -> AppUtils.copyToClipboard(inviteLabel));

        buttonBox.getChildren().addAll(generateInviteCodeButton, copyInviteCodeButton);
        inviteCodeBox.getChildren().addAll(inviteCodeLabel, inviteLabel);
        inviteCodeContainer.getChildren().addAll(inviteCodeBox, buttonBox);

        return inviteCodeContainer;
    }

    public static void showTaskDetailPopup(Stage primaryStage) {
        try {
            Parent taskDetailContent = loadFXML(EPopup.TASK_DETAILS.getFxmlPath());
            setPopupScreen(primaryStage, EPopup.TASK_DETAILS, new VBox(taskDetailContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showTablesPopup() {
        ListView<String> tablesPopupListView = new ListView<>();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(EString.MY_TABS.getString());

        VBox popupVBox = new VBox(10);
        popupVBox.getChildren().addAll(new Label(EString.MY_TABS.getString()), tablesPopupListView);
        popupVBox.setPadding(new Insets(10, 10, 10, 10));

        Scene popupScene = new Scene(popupVBox, EPopup.TABLE_LIST.getWidth(), EPopup.TABLE_LIST.getHeight());
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PopupService.class.getResource(fxmlPath));
        return fxmlLoader.load();
    }

}
