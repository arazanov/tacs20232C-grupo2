package com.example.telegrambot.api;

import com.fasterxml.jackson.databind.JsonNode;

public class MonitorApi extends ApiCalls {

    public JsonNode getMonitor(){
        return super.get("","/monitor");
    }

}