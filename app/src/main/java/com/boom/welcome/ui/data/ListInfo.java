package com.boom.welcome.ui.data;

import java.io.Serializable;
import java.util.List;

public class ListInfo implements Serializable {

    String time;
    String turneket;
    String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTurneket() {
        return turneket;
    }

    public void setTurneket(String turneket) {
        this.turneket = turneket;
    }
}
