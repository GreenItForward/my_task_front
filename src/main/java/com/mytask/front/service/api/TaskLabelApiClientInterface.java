package com.mytask.front.service.api;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import org.json.JSONException;

import java.util.List;

public interface TaskLabelApiClientInterface {
    void updateLabelToTask(Task task, LabelModel label);

    List<LabelModel> getLabelsByTaskId(int taskId, int projectId) throws JSONException;
}
