package com.mytask.front.service;

import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EPopup;
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
import java.util.Random;

import static com.mytask.front.utils.EIcon.*;

public class TabService {
    private static ScreenService screenService;

    // Singleton
    public static void init(Stage primaryStage) {
        screenService = ScreenService.getInstance(primaryStage);
    }

    public static HBox createColorTags(Random random) {
        Color color1 = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Color color2 = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        String tooltipStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-background-color: #000000; -fx-padding: 5px;";

        Rectangle colorRect1 = new Rectangle(20, 10, color1);
        Rectangle colorRect2 = new Rectangle(20, 10, color2);

        for (Color color : new Color[]{color1, color2}) {
            Rectangle colorRect = color.equals(color1) ? colorRect1 : colorRect2;
            String labelName = "Couleur: " + color.toString().substring(2, color.toString().length() - 2) + ", Titre: \"" + (random.nextInt(100) + 1) + "\"";
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
        }

        return new HBox(5, colorRect1, colorRect2);
    }

    public static Label createTitleLabel(Random random) {
        return new Label("[JAVA] Mise en place de JavaFX " + (random.nextInt(100) + 1));
    }

    public static HBox createDeadlineBox(Random random) {
        Image clockImage = CLOCK_ICON.getImage();
        Image clockImageChecked = CLOCK_ICON_CHECKED.getImage();

        ImageView clockImageView = new ImageView(clockImage);
        clockImageView.setFitHeight(15);
        clockImageView.setFitWidth(15);
        Label dueDateLabel = new Label((random.nextInt(30) + 1) + " avr");
        dueDateLabel.getStyleClass().add("dueDateLabel");

        Button dueDateButton = new Button();
        dueDateButton.setGraphic(clockImageView);
        dueDateButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        dueDateButton.setOnAction(e -> {
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
        PopupService.setPopupScreen(primaryStage, EPopup.MEMBERS);
    }
}
