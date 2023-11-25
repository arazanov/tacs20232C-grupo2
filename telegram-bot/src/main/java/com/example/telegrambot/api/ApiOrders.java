package com.example.telegrambot.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiOrders extends ApiCalls {

    public boolean patchOrderName(String userToken, String orderId, String name) {
        return super.patch(userToken, "/orders/" + orderId, "{\"description\": \"" + name + "\"}");
    }

    public boolean closeOrderApi(String orderId, String token) {
        return super.patch(token, "/orders/" + orderId, "{\"closed\": true}");
    }

    public boolean openOrderApi(String orderId, String token) {
        return super.patch(token, "/orders/" + orderId, "{\"closed\": false}");
    }

    public boolean shareOrderApi(String orderId, String token, String user) {
        JsonNode userShare = new UserApi().getUserByUsername(token, user);

        if (userShare != null) {
            String userBody = "";
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                userBody = "{\"user\":" + objectMapper.writeValueAsString(userShare) + "}";
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }

            return super.patch(token, "/orders/" + orderId, userBody);

        }
        return false;

    }

    public JsonNode createOrdersByUserId(String token) {
        return super.post(token, "/orders");
    }

    public boolean deleteOrder(String token, String id) {
        return super.delete(token, "/orders/" + id);
    }

    public JsonNode getOrderById(String id, String token) {
        return super.get(token, "/orders/" + id);
    }

    public JsonNode getOrdersByUserId(String token) {
        return super.get(token, "/orders");
    }

}
