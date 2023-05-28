package com.mytask.front.service.api;

import com.mytask.front.model.User;
import com.mytask.front.model.UserProject;
import com.mytask.front.utils.enums.ERole;
import org.json.JSONException;

import java.util.List;

public interface RoleApiClientInterface {
    UserProject joinProject(String codeJoin) throws JSONException;
    ERole changeRole(int userIdToChange, int projectId, String role) throws JSONException;
    List<User> getUsersByProject(int projectId) throws JSONException;
    ERole getRoleByProject(int projectId) throws JSONException;
    void excludeUser(int userIdToExclude, int projectId);
}
