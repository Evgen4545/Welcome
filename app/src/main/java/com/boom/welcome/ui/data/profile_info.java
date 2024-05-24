package com.boom.welcome.ui.data;

import java.util.ArrayList;

public class profile_info {
    private static profile_info dataObject = null;

    ArrayList<ListInfo> myvoiceInfos = new ArrayList<>();
    user_info my_user_info = new user_info();


    public ArrayList<ListInfo> getMyvoice() {
        return myvoiceInfos;
    }

    public void setMyvoice(ArrayList<ListInfo> Infos) {
        this.myvoiceInfos = Infos;
    }
    public user_info getMy_user_info() {
        return my_user_info;
    }

    public void setMy_user_info(user_info my_user_info) {
        this.my_user_info = my_user_info;
    }




    public static profile_info getInstance() {
        if (dataObject == null)
            dataObject = new profile_info();
        return dataObject;
    }
}

