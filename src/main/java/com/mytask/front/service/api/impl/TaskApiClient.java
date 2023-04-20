package com.mytask.front.service.api.impl;

import com.mytask.front.model.Task;
import com.mytask.front.service.api.TaskApiClientInterface;

public class TaskApiClient implements TaskApiClientInterface {
    @Override
    public void createTask(Task project) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Task getTaskById(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateTask(Task project) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteTask(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}