package com.boom.welcome.ui.data;

public class scanQr {
    boolean userQr = false;
    String userId = "";
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getUserQr (){
        return userQr;
    }
    public void setUserQr(boolean userQr){
        this.userQr=userQr;
    }
}
