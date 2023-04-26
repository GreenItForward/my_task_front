package com.mytask.front.configuration;

import com.mytask.front.model.LabelModel;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class AppConfiguration {

    // mock des labels pour le moment
    public static List<LabelModel> labels = Arrays.asList(
            new LabelModel("FRONT-END", Color.web("#FF0000")),
            new LabelModel("BACK-END", Color.web("#00FF00")),
            new LabelModel("TEST", Color.web("#0000FF")));

}
