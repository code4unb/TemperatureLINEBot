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
public abstract class MessageHandler extends MessageHandlerBase{
    @Getter
    private static List<Class<?>> Handlers;

    @Getter
    private static Map<Class<?>,MessageHandlerBase> HandlerInstances;

    static{
        Handlers=new ArrayList<Class<?>>();
        HandlerInstances = new HashMap<Class<?>, MessageHandlerBase>();

        RegisterHandler();
    }

    public MessageHandler(String keyPhrase, String... aliases) {
        super(keyPhrase, aliases);
    }

    public static void RegisterHandler(){
        log.info("Starting message handler register...");
        Handlers = new ClassGraph().enableAnnotationInfo().enableClassInfo().acceptPackages("com.code4unb.TemperatureLINEBot.message").scan()
                .getSubclasses(MessageHandlerBase.class.getName())
                    .filter(classInfo -> classInfo.hasAnnotation(Handler.class.getName()))
                            .loadClasses();
        Handlers.forEach(handler->{
            try {
                log.info("Registering MessageHandler:"+handler.getSimpleName());
                log.info(((MessageHandlerBase) handler.getConstructor().newInstance()).getKeyPhrase());
                HandlerInstances.put(handler, (MessageHandlerBase) handler.getConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            }
        });
    }
}
