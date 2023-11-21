package com.example.telegrambot.conversationThreads;

import com.example.telegrambot.UserData;
import com.example.telegrambot.UserState;
import com.example.telegrambot.api.UserApi;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManage {
    public final HashMap<Long, UserData> userDatas = new HashMap<>();

    public String login(UserData user){
        user.login();
        return "Por favor introduzca su username:";
    }
    public String signUp(UserData user){
        user.signUp();
        return "Por favor introduzca un username:";
    }

    public String waitUsernameSignUp(UserData user,String message){
        user.setUsername(message);
        user.setState(UserState.WAITING_EMAIL_SIGNUP);
        return "Por favor introduzca un email";
    }

    public String waitEmailSignUp(UserData user,String message){
        user.setMail(message);
        user.setState(UserState.WAITING_PASSWORD_SIGNUP);
        return "Por favor introduzca una contraseña";
    }

    public String waitPasswordSignUp(UserData user,String message){
        String loginToken = new UserApi().userSignUp(user.getUsername(),user.getMail(),message);
        user.setState(UserState.LOGOUT);
        if(loginToken!=null){
            return "SignUp exitoso, ahora puedes hacer /logIn .";
        }
        return "El signUp no se pudo efectuar de forma correcta.";
    }

    public String waitUsername(UserData user,String message){
        user.setUsername(message);
        user.setState(UserState.WAITING_PASSWORD);
        return "Por favor introduzca su contraseña";
    }

    public String waitPassword(UserData user,String message){
        String loginToken = new UserApi().userLogin(user.getUsername(),message);
        if((loginToken!=null) && (loginToken!="") && (loginToken!=" ")){
            user.setToken(loginToken);
            user.setState(UserState.LOGIN);
            return "Inicio de sesion exitoso.\nObtenga los comandos disponibles con /verComandos .";
        }
        user.setState(UserState.LOGOUT);
        return "El usuario o contraseña es incorrecto.";
    }


    public UserData getOrCreate(long chatId){
        UserData userData = userDatas.get(chatId);
        if(userData==null){
            userData = new UserData(chatId);
            userDatas.put(chatId,userData);
        }

        return userData;
    }

    public String verUsuario(UserData user){

        user.resetData();
        JsonNode jsonNode = new UserApi().getUserById(user.getToken());

        String mssg="Id de usuario: "+jsonNode.get("id").asText()+ "\nNombre de usuario: "+jsonNode.get("username").asText()+ "\nMail del usuario: "+jsonNode.get("email").asText()+ "\n\n/logOut"+ "\n/verComandos";

        return mssg;
    }

    public String logout(UserData user){
        userDatas.remove(user.getChatId());
        return "Sesion cerrada correctamente.\n\n/verComandos";
    }

    public String notLoggedResponse(UserData userData,String message){
        return message+"\n\n"+"Puedes intentar con alguno de estos comandos:\n/logIn\n/signUp";
    }

    public String defaultMessage(UserData userData,String message){
        String response = "";
        response += message;
        response += "\n\n";
        response += verComandos(userData);
        return response;
    }


    public String verComandos(UserData userData){
        String response = "Los comandos disponibles son los siguientes: ";
        for(String command:userData.availableCommands()){
            response+="\n\t• "+command;
        }
        return response;
    }


}
