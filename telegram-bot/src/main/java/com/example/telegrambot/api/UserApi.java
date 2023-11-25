package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;

public class UserApi extends ApiCalls {

    public String userLogin(String username, String password) {
        JsonNode json = super.post("", "/",
                "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}");
        if (json != null) {
            return json.get("token").asText();
        }
        return null;
    }

    public String userSignUp(String username, String email, String password) {

        return super.post("", "/users",
                "{\"username\":\"" + username + "\",\"email\":\"" + email + "\", \"password\":\"" + password + "\"}").get("token").asText();

    }

    public JsonNode getUserByUsername(String token, String username) {
        return super.get(token, "/users?username=" + username);
    }

    public JsonNode getUserById(String token) {
        return super.get(token, "/user");
    }

}
