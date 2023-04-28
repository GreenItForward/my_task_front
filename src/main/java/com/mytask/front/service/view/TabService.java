package com.mytask.front.service.view;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import com.mytask.front.utils.EString;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static com.mytask.front.utils.EIcon.*;

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

            System.out.println(task.getdeadlineDatePicker().getValue().format(DateTimeFormatter.ofPattern(EString.DATE_FORMAT.toString())));

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


    public static TextField createAssignedToField(Random random) {
        TextField assignedToField = new TextField("Personne " + (random.nextInt(10) + 1));
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
}