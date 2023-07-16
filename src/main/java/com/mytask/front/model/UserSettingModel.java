package com.mytask.front.model;

import static com.mytask.front.service.AppService.colorToHexString;

public class UserSettingModel {
    private int id;
    private User user;
    private String background;

    public UserSettingModel() {
        this.background = "";
    }

    public UserSettingModel(int id, User user, String background) {
        this.id = id;
        this.user = user;
        this.background = "";
    }

    public UserSettingModel(String background) {
        this.id = 0;
        this.user = new User();
        this.background = background;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getBackground() {
        return background;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBackground(String background) {
        this.background = background;
    }


    @Override
    public String toString() {
        return "UserSettingModel{" +
                "id=" + id +
                ", user=" + user +
                ", background='" + background + '\'';
    }

    public String toJSON() {
        return "{\"background\":\"" + background + "\"}";
    }
}
