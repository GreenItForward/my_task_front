package com.mytask.front.controller;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.ProjectApiClientInterface;
import com.mytask.front.service.api.impl.ProjectApiClient;
import com.mytask.front.service.view.ShowAllTabService;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ShowAllTabController {

    @FXML
    private Label myTablesLabel, tableInfoLabel, descriptionLabel, tableTitleLabel, tableDescriptionLabel;

    @FXML
    private Button backToMenuBtn, openTableBtn;

    @FXML
    private ListView tablesListView;

    private ScreenService screenService;
    private ProjectApiClient projectApiClient;

    @FXML
    public void initialize() throws JSONException {
        this.projectApiClient = ProjectApiClient.getInstance();
        ArrayList<Project> projects = projectApiClient.getProjectByUser();
        ShowAllTabService.getInstance().setProjects(projects);

        for(Project project : projects) {
            tablesListView.getItems().add(project.getNom());
        }

        // ajout des listeners
        tablesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Project project = projects.get(tablesListView.getSelectionModel().getSelectedIndex());
                tableTitleLabel.setText(project.getNom());
                tableDescriptionLabel.setText(project.getDescription());
            }
        });

        backToMenuBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) backToMenuBtn.getScene().getWindow());
            }
        });
        myTablesLabel.setText(EString.MY_TABS.toString());
        tableInfoLabel.setText(EString.INFORMATION_TAB.toString());
        descriptionLabel.setText(EString.DESCRIPTION.toString());
        backToMenuBtn.setText(EString.BACK_TO_MENU.toString());
        openTableBtn.setText(EString.OPEN_TABLE.toString());
        openTableBtn.setOnAction(event -> screenService.setScreen(EPage.SHOW_TAB));
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
    }

}