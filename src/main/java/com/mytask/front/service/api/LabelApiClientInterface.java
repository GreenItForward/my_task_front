package com.mytask.front.service.api;


import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import org.json.JSONException;

import java.util.List;

public interface LabelApiClientInterface {
    LabelModel createLabel(LabelModel label) throws JSONException;
    void addLabel(LabelModel label);
    void removeLabel(LabelModel label);
    void deleteLabel(LabelModel label);
    void updateLabel(LabelModel label) throws JSONException;
    List<LabelModel> getLabelsByProjectId(int id) throws JSONException;
    List<LabelModel> getLabels(Project project) throws JSONException;
}
