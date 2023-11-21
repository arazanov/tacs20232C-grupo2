package com.example.telegrambot;

import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    public String token;
    public long chatid;
    public UserState state;

    public String mail;
    public String username;
    public String pedidoId;
    public String itemId;

    public UserData(long chatId){
        this.chatid=chatId;
        this.username=null;
        this.mail=null;
        this.token=null;
        this.pedidoId=null;
        this.itemId=null;
        this.state=UserState.LOGOUT;
    }

    public void login(){
        this.username=null;
        this.mail=null;
        this.token=null;
        this.pedidoId=null;
        this.itemId=null;
        this.state=UserState.WAITING_ID;
    }
    public void signUp(){
        this.username=null;
        this.mail=null;
        this.token=null;
        this.pedidoId=null;
        this.itemId=null;
        this.state=UserState.WAITING_USERNAME_SIGNUP;
    }


    public void resetData(){
        this.state=UserState.LOGIN;
        this.pedidoId=null;
        this.itemId=null;
    }

    public boolean isLogin(){
        boolean answer =  this.state.isLogin(this.state);
        System.out.println(answer);
        return answer;
    }

    public List<String> availableCommands(){
        List<String> availables = new ArrayList<>();
        availables.add("/logIn (Inicia Sesion).");
        availables.add("/signUp (Crea una cuenta).");
        availables.add("/start (Ver mensaje de bienvenida).");
        availables.add("/stats (Ver estadisticas de la aplicacion).");
        availables.add("/verComandos (Lista todos los comandos disponibles).");
        if (this.isLogin()){
            availables.add("/logOut (Cierra sesion).");
            availables.add("/verUsuario (Ver informacion del usuario actual).");
            availables.add("/verPedidos (Lista todos los pedidos a los que tienes acceso).");
            availables.add("/crearPedido (Crea nuevo pedido).");
        }
        if (this.getItemId()!=null){
            availables.add("/sumarItem (Suma N unidades al item accedido).");
            availables.add("/restarItem (Resta N unidades al item accedido).");
            availables.add("/cambiarDescipcionItem (Cambia la descripcion del item accedido).");
            availables.add("/cambiarUnidadItem (Cambia la unidad del item accedido).");
            availables.add("/volverAPedido (Vuelve a la vista del pedido del item accedido).");
        }else {
            if(this.getPedidoId()!=null){
                availables.add("/cambiarNombreAPedido (Cambiar nombre de pedido accedido).");
                availables.add("/agregarItemAPedido (Crea nuevo item en el pedido accedido).");
            }
        }
        return availables;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // Getter y Setter para el campo 'username'
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getChatId() {
        return chatid;
    }

    public void setchatId(long chatid) {
        this.chatid = chatid;
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
        this.itemId = null;
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
