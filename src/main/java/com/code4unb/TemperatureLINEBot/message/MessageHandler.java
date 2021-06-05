package com.code4unb.TemperatureLINEBot.message;

import com.linecorp.bot.model.message.Message;
import io.github.classgraph.ClassGraph;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public abstract class MessageHandler {
    @Autowired
    private BuildProperties properties;

    @Getter
    private final String KeyPhrase;

    @Getter
    private final String[] Aliases;

    @Getter
    private static List<Class<?>> Handlers;

    @Getter
    private static Map<Class<?>,MessageHandler> HandlerInstances;

    static{
        Handlers=new ArrayList<Class<?>>();
        HandlerInstances = new HashMap<Class<?>, MessageHandler>();

        RegisterHandler();
    }
    public MessageHandler(String keyPhrase,String... aliases){
        KeyPhrase = keyPhrase;
        Aliases = aliases;
    }

    public abstract Message HandleMessage(ReceivedMessage message);

    public boolean shouldHandle(String phrase){
        if(KeyPhrase.equalsIgnoreCase(phrase))return true;
        return Arrays.stream(Aliases).anyMatch(x->x.equalsIgnoreCase(phrase));
    }

    public static void RegisterHandler(){
        log.info("Starting message handler register...");
        Handlers = new ClassGraph().enableAnnotationInfo().enableClassInfo().acceptPackages("com.code4unb.TemperatureLINEBot.message").scan()
                .getSubclasses(MessageHandler.class.getName())
                    .filter(classInfo -> classInfo.hasAnnotation(Handler.class.getName()))
                            .loadClasses();
        Handlers.forEach(handler->{
            try {
                log.info("Registering MessageHandler:"+handler.getSimpleName());
                HandlerInstances.put(handler.getClass(), (MessageHandler) handler.getConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            }
        });
    }
}
