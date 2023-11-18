package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemsApi extends apiCalls{

    String myUrl ="http://backend:8080";

    public boolean putItemDescription(String token,String itemId,String description){
        JsonNode item = getItemById(itemId,token);
        if(item==null) return false;
        if(item instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) item;
            objectNode.put("description", description);
            return putItem(token,objectNode);
        }else return false;
    }

    public boolean putItemUnit(String token,String itemId,String unit){
        JsonNode item = getItemById(itemId,token);
        if(item==null) return false;
        if(item instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) item;
            objectNode.put("unit", unit);
            return putItem(token,objectNode);
        }else return false;
    }

    public boolean putItemModQty(String token,String itemId,Integer qty){
        JsonNode item = getItemById(itemId,token);
        if(item==null) return false;
        if(item instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) item;
            objectNode.put("quantity", objectNode.get("quantity").asInt()+qty);
            return putItem(token,objectNode);
        }else return false;

    }

    public boolean putItemQuantity(String token,String itemId,Integer qty){
        JsonNode item = getItemById(itemId,token);
        if(item==null) return false;
        if(item instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) item;
            objectNode.put("quantity", qty);
            return putItem(token,objectNode);
        }else return false;
    }

    private boolean putItem(String token,ObjectNode item){
        try {
            String id = item.get("id").asText();
                ObjectMapper objectMapper = new ObjectMapper();
                String body = objectMapper.writeValueAsString(item);
                return super.put(token,"/items/"+id,body);
        }catch (Exception e){
            return false;
        }
    }

    public JsonNode addItemApi(String pedidoId, String token){
        return super.post(token,"/orders/"+pedidoId+"/items");
    }


    public JsonNode getItemById(String id,String token) {
        return super.get(token,"/items/" + id);
    }


    public JsonNode getOrderItems(String pedidoId,String token){
        return super.get(token,"/orders/"+pedidoId+"/items");
    }

}
