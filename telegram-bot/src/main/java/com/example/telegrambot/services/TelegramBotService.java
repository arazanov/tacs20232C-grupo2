package com.example.telegrambot.services;

import com.example.telegrambot.PedidosBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TelegramBotService {


    public TelegramBotService(@Value("${bot.token}") String token,@Value("${bot.username}") String username){

        System.out.println(token);
        System.out.println(username);


        try {
            PedidosBot pedidosBot = new PedidosBot(token,username);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(pedidosBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
