package com.mytask.front.service.api;

import com.mytask.front.model.User;
import com.mytask.front.model.UserProject;
import org.json.JSONException;

import java.util.List;

public interface RoleApiClientInterface {
    UserProject joinProject(String codeJoin) throws JSONException;
    void changeRole(int userIdToChange, int projectId, String role) throws JSONException;
    List<User> getUsersByProject(int projectId) throws JSONException;
}
