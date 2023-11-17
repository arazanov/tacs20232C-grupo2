package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

public class UserApi extends apiCalls{

    String myUrl ="http://backend:8080";



    public String userLogin(String username,String password) {
        JsonNode json = super.post("","/","{\"username\":\""+username+"\", \"password\":\""+password+"\"}");
        if (json!=null){
            return json.get("token").asText();
        }
        return null;
    }

    public String userSignUp(String username,String email,String password) {

        return super.post("","/users","{\"username\":\""+username+"\",\"email\":\""+email+"\", \"password\":\""+password+"\"}").get("token").asText();

    }

    public JsonNode getUserByUsername(String token,String username) {
        return super.get(token,"/users?username="+username);
    }

    public JsonNode getUserById(String token) {
        return super.get(token,"/user");
    }

}
