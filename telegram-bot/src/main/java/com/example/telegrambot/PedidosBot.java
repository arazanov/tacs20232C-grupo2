package com.example.telegrambot;

import org.springframework.core.env.Environment;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

public class PedidosBot extends AbilityBot {

    MessageHandler messageHandler;
    public static String BOT_TOKEN = "6474434866:AAH4dMSOvG3M-yrquvykVh_pZxKeJASvQqo";
    public static String BOT_USERNAME  = "PedidosCompartidosbot";


    public Ability login() {
        return Ability
                .builder()
                .name("login")
                .info("login")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> messageHandler.login(ctx))
                .build();
    }

    public Ability viewOrders() {
        return Ability
                .builder()
                .name("verPedidos")
                .info("ver pedidos")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> messageHandler.viewOrders(ctx))
                .build();
    }

    public PedidosBot() {
        super(BOT_TOKEN,BOT_USERNAME);
        this.messageHandler = new MessageHandler(silent);
    }

    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> messageHandler.replyToButtons(upd);
        return Reply.of(action, Flag.TEXT, upd -> messageHandler.userIsActive(getChatId(upd)));
    }

    @Override
    public long creatorId() {
        return 1L;
    }
}