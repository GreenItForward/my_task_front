package com.mytask.front.service.api;


import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.model.UserSettingModel;
import com.mytask.front.utils.enums.EAuthEndpoint;

public interface UserSettingApiClientInterface {
    UserSettingModel getUserSettings() throws AuthException;
    void postUserSetting(UserSettingModel userSettingModel) throws AuthException;
}