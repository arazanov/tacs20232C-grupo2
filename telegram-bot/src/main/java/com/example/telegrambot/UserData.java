package com.example.telegrambot;

import org.apache.catalina.User;

public class UserData {
    public String token;
    public long chatid;
    public UserState state;
    public String pedidoId;
    public String itemId;

    public UserData(String token){this.token=token}

    public long getChatId() {
        return chatid;
    }

    public void setchatId(long chatid) {
        this.chatid = chatid
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter y Setter para el campo 'state'
    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    // Getter y Setter para el campo 'pedidoId'
    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    // Getter y Setter para el campo 'itemId'
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
