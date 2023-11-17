package com.example.telegrambot;

import java.util.ArrayList;
import java.util.List;

public enum UserState {
    WAITING_USERNAME_SIGNUP,
    WAITING_EMAIL_SIGNUP,
    WAITING_PASSWORD_SIGNUP,
    WAITING_ID,
    WAITING_PASSWORD,
    LOGOUT,
    WAITING_SHARE_ID,
    LOGGED_IN,
    WAITING_ITEM_DESCRIPTION,
    WAITING_ITEM_QUANTITY,
    WAITING_ORDER_NAME,
    WAITING_ITEM_UNIT,
    MOD_SUM_ITEM,
    MOD_DEC_ITEM,
    MOD_ITEM_DESC,
    MOD_ITEM_UNIT,
    BASE,
    LOGIN;

    public boolean isLogin(UserState userState){
        List<UserState> logouts = new ArrayList<>();
        logouts.add(WAITING_EMAIL_SIGNUP);
        logouts.add(WAITING_PASSWORD_SIGNUP);
        logouts.add(WAITING_USERNAME_SIGNUP);
        logouts.add(WAITING_ID);
        logouts.add(WAITING_PASSWORD);
        logouts.add(LOGOUT);
        System.out.println(logouts.toString());
        System.out.println(userState.toString());
        if(logouts.contains(userState)) return false;
        return true;
    }
}
