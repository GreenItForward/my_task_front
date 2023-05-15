package com.mytask.front.service.api;

import com.mytask.front.model.Project;
import com.mytask.front.model.User;
import org.json.JSONException;

public interface RoleApiClientInterface {
    Project joinProject(String codeJoin) throws JSONException;
    void changeRole(int userIdToChange, int projectId, String role) throws JSONException;
    User[] getUsersByProject(int projectId) throws JSONException;
}
