package com.mytask.front.service.api;


import com.mytask.front.model.LabelModel;
import javafx.scene.control.Label;

import java.util.List;

public interface LabelApiClientInterface {
    void createLabel(LabelModel label);
    void addLabel(LabelModel label);
    void removeLabel(LabelModel label);
    void updateLabel(LabelModel label);
    List<LabelModel> getLabels();
}
