package com.mytask.front.controller;

import com.mytask.front.model.Project;
import com.mytask.front.service.api.impl.ProjectApiClient;
import com.mytask.front.service.api.impl.RoleApiClient;
import com.mytask.front.service.view.ProjectTabService;
import com.mytask.front.service.view.ShowAllTabService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.enums.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.enums.EString;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONException;

import java.util.ArrayList;

public class ShowAllTabController {

    @FXML
    private Label myTablesLabel, tableInfoLabel, descriptionLabel, tableTitleLabel, tableDescriptionLabel;

    @FXML
    private Button backToMenuBtn, openTableBtn;

    @FXML
    private ListView tablesListView;

    private ScreenService screenService;
    private ProjectApiClient projectApiClient;
    ArrayList<Project> projects;

    private static ShowAllTabController instance;

    private ShowAllTabController() {
    	instance = this;
    }

    public static ShowAllTabController getInstance() {
    	if (instance == null) {
    		instance = new ShowAllTabController();
    	}

        return instance;
    }

    @FXML
    public void initialize() throws JSONException {
        initData();
        configureButtons();
        setTextForUIElements();
    }


    private void initData() throws JSONException {
        this.projectApiClient = ProjectApiClient.getInstance();
        projects = projectApiClient.getProjectByUser();
        ShowAllTabService.getInstance().setProjects(projects);

        ObservableList<Project> observableProjects = FXCollections.observableArrayList(projects);
        tablesListView.setItems(observableProjects);
        tablesListView.setCellFactory(param -> new ListCell<Project>() {
            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);

                if (empty || project == null) {
                    setText(null);
                } else {
                    setText(project.getNom());
                }
            }
        });
    }


    private void configureButtons() {
        configureListView();
        openTableBtn.setOnAction(e -> {
            Project selectedProject = (Project) tablesListView.getSelectionModel().getSelectedItem();
            if (selectedProject != null) {
                try {
                    UserService.getCurrentUser().setRole(RoleApiClient.getInstance().getRoleByProject(selectedProject.getId()));
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                ProjectTabService projectTabService = ProjectTabService.getInstance();
                projectTabService.openProject(selectedProject);
            }
        });

        backToMenuBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) backToMenuBtn.getScene().getWindow());
            }
        });
    }

    private void configureListView() {
        tablesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTableInfo(newValue);
        });

        tablesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Project selectedProject = (Project) tablesListView.getSelectionModel().getSelectedItem();
                if (selectedProject != null) {
                    ProjectTabService projectTabService = ProjectTabService.getInstance();
                    projectTabService.openProject(selectedProject);
                }
            }
        });
    }

    private void updateTableInfo(Object newValue) {
        if (newValue != null) {
            Project project = (Project) newValue;
            tableTitleLabel.setText(project.getNom());
            tableDescriptionLabel.setText(project.getDescription());
            openTableBtn.setDisable(false);
        } else {
            tableTitleLabel.setText("");
            tableDescriptionLabel.setText("");
            openTableBtn.setDisable(true);
        }
    }


    private void setTextForUIElements() {
        myTablesLabel.setText(EString.MY_TABS.toString());
        tableInfoLabel.setText(EString.INFORMATION_TAB.toString());
        descriptionLabel.setText(EString.DESCRIPTION.toString());
        backToMenuBtn.setText(EString.BACK_TO_MENU.toString());
        openTableBtn.setText(EString.OPEN_TABLE.toString());
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public void resetProjectList() {
        tablesListView.getItems().clear();
    }

    public void updateProjectList() {
        try {
            projects = projectApiClient.getProjectByUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ObservableList<Project> observableProjects = FXCollections.observableArrayList(projects);
        tablesListView.setItems(observableProjects);
        tableTitleLabel.setText("");
        tableDescriptionLabel.setText("");
        openTableBtn.setDisable(true);
        tablesListView.getSelectionModel().clearSelection();
    }

}