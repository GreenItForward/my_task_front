package com.mytask.front.service.api;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;

public interface TaskLabelApiClientInterface {
    void updateLabelToTask(Task task, LabelModel label);
}
