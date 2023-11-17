package com.example.telegrambot.api;
import com.example.telegrambot.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiOrders extends apiCalls{



    String myUrl ="http://backend:8080";


    public boolean patchOrderName(String userToken,String orderId,String name){
        return super.patch(userToken,"/orders/"+orderId,"{\"description\": \""+name+"\"}");
    }


    public boolean closeOrderApi(String orderId,String token){
        return super.patch(token,"/orders/"+orderId,"{\"closed\": true}");
    }

    public boolean shareOrderApi(String orderId,String token,String user){
        JsonNode userShare = new UserApi().getUserByUsername(token,user);

        if(userShare!=null) {
            String userBody = "";
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                userBody = "{\"user\":" + objectMapper.writeValueAsString(user) + "}";
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }

            return super.patch(token,"/orders/"+orderId,userBody);

        }
        return false;


    }




    public JsonNode createOrdersByUserId(String token) {
        return super.post(token,"/orders");
    }




    public JsonNode getOrderById(String id,String token) {
        return super.get(token,"/orders/"+id);
    }



    public JsonNode getOrdersByUserId(String token) {
        return super.get(token,"/orders");
    }


}
