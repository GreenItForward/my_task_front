package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.exception.AuthException;
import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.enums.EString;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.mytask.front.utils.enums.EIcon.*;

public class TabService {
    private static PopupService popupService;

    // Singleton
    public static void init(Stage primaryStage) {
        ScreenService screenService = ScreenService.getInstance(primaryStage);
        popupService = popupService.getInstance();

    }

    public static Rectangle createColorRectangle(String title, Color color, int taskID) {
        String tooltipStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-background-color: #000000; -fx-padding: 5px;";
        Rectangle colorRect = new Rectangle(20, 10, color);

        String labelName = "Couleur: " + color.toString().substring(2, color.toString().length() - 2) + ", Titre: \"" + title + "\"";
        colorRect.setId(String.format("label-%s-%s", title, taskID));
        Tooltip tooltip = new Tooltip(labelName);
        tooltip.setStyle(tooltipStyle);
        Tooltip.install(colorRect, tooltip);
        tooltip.setStyle(tooltipStyle);
        colorRect.setArcWidth(10);
        colorRect.setArcHeight(10);
        colorRect.setOnMouseEntered(e -> colorRect.setOpacity(0.5));
        colorRect.setOnMouseExited(e -> colorRect.setOpacity(1));
        tooltip.setHideDelay(Duration.seconds(0.3));
        tooltip.setShowDelay(Duration.seconds(0.2));

        return colorRect;
    }

    public static HBox createColorTags(Task task) {
        final HBox result = new HBox();
        result.setSpacing(5); // Ajouter un espacement entre les étiquettes
        result.setId("hbox-labels");
        List<LabelModel> labels = task.getLabels();
        labels.forEach(label -> {
            Rectangle colorRect = createColorRectangle(label.getNom(), label.getCouleur(), task.getId());
            result.getChildren().add(colorRect);
        });

        return result;
    }

    public static void updateColorTags(Task task) {
        HBox hbox = task.getLabelBox();
        hbox.getChildren().clear();
        List<LabelModel> labels = task.getLabels();
        labels.forEach(label -> {
            Rectangle colorRect = createColorRectangle(label.getNom(), label.getCouleur(), task.getId());
            hbox.getChildren().add(colorRect);
        });
    }

    public static Label createTitleLabel(Random random) {
        return new Label("[JAVA] Mise en place de JavaFX " + (random.nextInt(100) + 1));
    }

    public static HBox createDeadlineBox(Task task) {
        Image clockImage = CLOCK_ICON.getImage();
        Image clockImageChecked = CLOCK_ICON_CHECKED.getImage();

        ImageView clockImageView = new ImageView(clockImage);
        clockImageView.setFitHeight(15);
        clockImageView.setFitWidth(15);
        Label dueDateLabel = new Label();
        dueDateLabel.getStyleClass().add("dueDateLabel");
        StringProperty formattedDateProperty = new SimpleStringProperty();
        task.getdeadlineDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String formattedDate = task.parseDueDate(newValue.format(DateTimeFormatter.ofPattern(EString.DATE_FORMAT.toString())));
                formattedDateProperty.set(formattedDate);
            } else {
                formattedDateProperty.set(null);
            }
        });

        dueDateLabel.textProperty().bind(formattedDateProperty);

        if (task.getdeadlineDatePicker().getValue() != null) {
            String formattedDate = task.parseDueDate(task.getdeadlineDatePicker().getValue().format(DateTimeFormatter.ofPattern(EString.DATE_FORMAT.toString())));
            formattedDateProperty.set(formattedDate);
        }

        Button dueDateButton = new Button();
        dueDateButton.setGraphic(clockImageView);
        dueDateButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        dueDateButton.visibleProperty().bind(task.getdeadlineDatePicker().valueProperty().isNotNull());
        dueDateLabel.visibleProperty().bind(task.getdeadlineDatePicker().valueProperty().isNotNull());

        dueDateButton.setOnAction(e -> {
            if (task.getdeadlineDatePicker().getValue() == null) {
                return;
            }

            if (clockImageView.getImage() == clockImage) {
                clockImageView.setImage(clockImageChecked);
                dueDateLabel.getStyleClass().add("dueDateLabelChecked");
            } else {
                clockImageView.setImage(clockImage);
                dueDateLabel.getStyleClass().remove("dueDateLabelChecked");
            }
        });

        return new HBox(5, dueDateButton, dueDateLabel);
    }


    public static TextField createAssignedToField() {
        TextField assignedToField = new TextField("");
        assignedToField.setEditable(false);
        assignedToField.getStyleClass().add("dueDateLabel");
        assignedToField.setOnMouseEntered(e -> assignedToField.setCursor(Cursor.HAND));
        return assignedToField;
    }

    public static ImageView createEditImageView() {
        Image editImage = PENCIL_ICON.getImage();

        ImageView editImageView = new ImageView(editImage);
        editImageView.setFitHeight(15);
        editImageView.setFitWidth(15);
        editImageView.setVisible(false); // initially invisible

        return editImageView;
    }

    public static void showMembers(Stage primaryStage) {
        popupService.showMemberPopup(primaryStage);
    }

    public static void showInviteCode(Stage primaryStage) {
        popupService.showInviteCodePopup(primaryStage);
    }

    public static void showLabels(Stage window, Task task) {
        popupService.showLabelPopup(window, task);
    }

    public static TextField createAddTaskField(VBox taskList) {
        TextField addTaskField = new TextField(EString.ADD_TASK.toString());
        addTaskField.getStyleClass().add("add-task-field");
        // Rendre le champ non modifiable jusqu'à ce que l'utilisateur clique dessus
        addTaskField.setEditable(false);
        addTaskField.setOnMouseClicked(event -> {
            if (!addTaskField.isEditable()) {
                addTaskField.setEditable(true);
                addTaskField.setText(""); // Vider le texte lorsqu'il est cliqué
            }
        });

        // lorsque l'utilisateur appuie sur Entrée après avoir modifié le texte, on ajoute une nouvelle tâche
        addTaskField.setOnAction(event -> {
            String taskText = addTaskField.getText().trim();
            if (!taskText.isBlank() && !taskText.equals(EString.ADD_TASK.toString())) {
                Task taskNew = new Task();
                taskList.setUserData(taskNew);
                taskNew.setStatus(taskList.getId());
                taskNew.setTitle(taskText);
                taskNew.setProjectID(ShowTabController.getInstance().getProject().getId());
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return TaskApiClient.getInstance().createTask(taskNew);
                    } catch (JSONException | AuthException e) {
                        throw new RuntimeException(e);
                    }
                }).thenAcceptAsync(createdTask -> {
                    HBox newTask = ShowTabController.getInstance().createTask(createdTask, taskText);
                    taskList.getChildren().add(taskList.getChildren().size(), newTask);
                    createdTask.setTaskBox(newTask);
                    taskList.setUserData(createdTask);
                    ShowTabController.getInstance().setCurrentTask(createdTask);
                    addTaskField.setEditable(false);
                    addTaskField.setText(EString.ADD_TASK.toString());
                }, Platform::runLater).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            }
        });

        addTaskField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(!newValue) && addTaskField.getText().isEmpty()) {
                addTaskField.setText(EString.ADD_TASK.toString());
                addTaskField.setEditable(false);
            }
        });

        return addTaskField;
    }


    public static void resetTab(VBox todoTasksList, VBox inProgressTasksList, VBox doneTasksList) {
        //clear sauf le premier élément
        todoTasksList.getChildren().removeIf(node -> todoTasksList.getChildren().indexOf(node) != 0);
        inProgressTasksList.getChildren().removeIf(node -> inProgressTasksList.getChildren().indexOf(node) != 0);
        doneTasksList.getChildren().removeIf(node -> doneTasksList.getChildren().indexOf(node) != 0);

        todoTasksList.setUserData(null);
        inProgressTasksList.setUserData(null);
        doneTasksList.setUserData(null);

        ShowTabController.getInstance().setCurrentTask(null);
    }
}