package com.mytask.front.service.view;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.LabelApiClient;
import com.mytask.front.utils.EString;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LabelService {
    private static LabelService instance;
    private ShowAllTabService showAllTabService;

    private LabelService() {
        showAllTabService = ShowAllTabService.getInstance();
    }

    public static LabelService getInstance() {
        if (instance == null) {
            instance = new LabelService();
        }
        return instance;
    }

    protected VBox createEditLabelContent() {
        VBox labelContainer = new VBox();
        labelContainer.setSpacing(10);
        labelContainer.setStyle("-fx-padding: 10;");
        List<LabelModel> originalLabels = showAllTabService.getProjects().get(0).getLabels();
        List<LabelModel> modifiableLabels = new ArrayList<>(originalLabels);

        List<LabelModel> labels = showAllTabService.getProjects().get(0).getLabels();
        for (LabelModel label : labels) {
            HBox labelInfo = createLabelProjectInfo(label, labelContainer);
            labelContainer.getChildren().add(labelInfo);
        }

        // create a new vbox for the add button
        VBox addLabelContainer = new VBox();
        addLabelContainer.setSpacing(10);
        addLabelContainer.setStyle("-fx-padding: 10;");

        Label labelTitle = new Label(EString.ADD_LABEL.toString());
        labelTitle.getStyleClass().add("popup-title");

        Button addLabelButton = new Button(EString.ADD_LABEL.toString());
        addLabelButton.getStyleClass().add("button-add-label");

        addLabelButton.setOnAction(e -> {
            LabelModel label = new LabelModel("", Color.WHITE);
            HBox labelInfo = createLabelProjectInfo(label, labelContainer);
            labelContainer.getChildren().add(labelContainer.getChildren().size() - 1, labelInfo);
        });

        // enregistrer les modifications
        Button saveButton = new Button(EString.SAVE.toString());
        saveButton.getStyleClass().add("button-save");
        saveButton.setOnAction(e -> {
            //   originalLabels.clear();
            //  originalLabels.addAll(modifiableLabels);
        });

        addLabelContainer.getChildren().addAll(labelTitle, addLabelButton);

        labelContainer.getChildren().addAll(addLabelContainer);

        return labelContainer;
    }


    protected VBox createLabelContent(Task task) {
        VBox labelContainer = new VBox();
        labelContainer.setSpacing(10);
        labelContainer.setStyle("-fx-padding: 10;");

        List<LabelModel> labels = showAllTabService.getProjects().get(0).getLabels();

        for (LabelModel label : labels) {
            TextField nameLabel = new TextField(label.getNom());
            ColorPicker colorPicker = new ColorPicker(label.getCouleur());

            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(task.getLabels().contains(label));

            HBox labelInfo = new HBox(10);
            labelInfo.getChildren().addAll(nameLabel, colorPicker, checkBox);
            labelContainer.getChildren().add(labelInfo);

            checkBox.setOnAction(e -> toggleLabel(checkBox, task, label));

            nameLabel.setDisable(true);
            colorPicker.setDisable(true);
        }

        return labelContainer;
    }

    private HBox createLabelProjectInfo(LabelModel label, VBox labelContainer) {
        HBox labelInfo = new HBox(10);
        List<LabelModel> originalLabels = new ArrayList<>((showAllTabService.getProjects().get(0).getLabels()));
        List<LabelModel> modifiableLabels = new ArrayList<>(originalLabels);

        TextField nameLabel = new TextField(label.getNom());
        ColorPicker colorPicker = new ColorPicker(label.getCouleur());

        Button deleteButton = new Button(EString.SUPPRIMER.toString());
        deleteButton.getStyleClass().add("button-delete");

        deleteButton.setOnAction(e -> {
            modifiableLabels.remove(label);
            showAllTabService.getProjects().get(0).setLabels(modifiableLabels);
            labelContainer.getChildren().remove(labelInfo);

            if (originalLabels.contains(label)) {
                LabelApiClient.getInstance().deleteLabel(label);
                originalLabels.remove(label);
            }
        });

        nameLabel.textProperty().addListener((observable, oldValue, newValue) -> nameLabel.setOnAction(e -> {
            if (!nameLabel.getText().isEmpty()) {
                int index = modifiableLabels.indexOf(label);
                if (index == -1) {
                    modifiableLabels.add(label);
                    index = modifiableLabels.indexOf(label);
                    showAllTabService.getProjects().get(0).setLabels(modifiableLabels);
                }

                label.setNom(newValue);
                modifiableLabels.set(index, label);
                showAllTabService.getProjects().get(0).getLabels().get(index).setNom(newValue);
            }
        }));

        colorPicker.valueProperty().addListener((observableColorPicker, oldValueColorPicker, newValueColorPicker) -> {
            int indexColorPicker = modifiableLabels.indexOf(label);
            if (indexColorPicker == -1) {
                modifiableLabels.add(label);
                indexColorPicker = modifiableLabels.indexOf(label);
                showAllTabService.getProjects().get(0).setLabels(modifiableLabels);
            }

            label.setCouleur(newValueColorPicker);
            modifiableLabels.set(indexColorPicker, label);
            showAllTabService.getProjects().get(0).setLabels(modifiableLabels);
        });

        labelInfo.getChildren().addAll(nameLabel, colorPicker, deleteButton);

        return labelInfo;
    }

    protected static void toggleLabel(CheckBox toggleCheckBox, Task task, LabelModel label) {
       if (task.getLabels().contains(label)) {
            task.getLabels().remove(label);
         //   LabelApiClient.getInstance().removeLabel(label);
        } else {
            task.getLabels().add(label);
          //  LabelApiClient.getInstance().addLabel(label);
        }
        PopupService.getInstance().updateToggleButton(toggleCheckBox, task, label);
    }

}
