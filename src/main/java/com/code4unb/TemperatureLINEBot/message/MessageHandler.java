package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class MessageHandler {
    @Getter
    private final String KeyPhrase;

    @Getter
    private final String[] Aliases;

    @Getter
    private static ArrayList<Class<? extends MessageHandler>> Handlers;

    @Getter
    private static Map<Class<? extends MessageHandler>,MessageHandler> HandlerInstances;

    static{
        Handlers=new ArrayList<Class<? extends MessageHandler>>();
        HandlerInstances = new HashMap<Class<? extends MessageHandler>, MessageHandler>();
    }
    public MessageHandler(String keyPhrase,String... aliases){
        KeyPhrase = keyPhrase;
        Aliases = aliases;
    }
    public boolean shouldHandle(String phrase){
        if(KeyPhrase.equalsIgnoreCase(phrase))return true;
        return Arrays.stream(Aliases).anyMatch(x->x.equalsIgnoreCase(phrase));
    }
    public abstract Message HandleMessage(ReceivedMessage message);

    public static void RegisterHandler(Class<? extends MessageHandler> type){
        Handlers.add(type);
        try {
            HandlerInstances.put(type,type.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public static void RegisterHandler(MessageHandler handler){
        Class<? extends MessageHandler> type = handler.getClass();
        Handlers.add(type);
        HandlerInstances.put(type,handler);
    }
}
