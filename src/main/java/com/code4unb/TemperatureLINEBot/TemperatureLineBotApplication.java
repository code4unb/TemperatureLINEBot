package com.code4unb.TemperatureLINEBot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

@SpringBootApplication
public class TemperatureLineBotApplication {
	public static BuildProperties BuildProperties;

	@Value("${line.bot.channel-token}")
	private String token;

	private static LineMessagingClient client;

	public static void main(String[] args) {
		SpringApplication.run(TemperatureLineBotApplication.class, args);
		initialize();
	}

	private static void initialize(){
		//client = LineMessagingClient.builder(token).build();
		Properties prop1 = new Properties();
		Properties prop2 = new Properties();
		try {
			prop1.load(TemperatureLineBotApplication.class.getClassLoader().getResourceAsStream("META-INF/build-info.properties"));
			Set keys = prop1.keySet();
			Iterator ite = keys.iterator();
			while(ite.hasNext()){
				String key = (String)ite.next();
				Object value = prop1.get(key);
				prop2.setProperty(((String)key).replace("build.",""),(String)value);
			}
			BuildProperties = new BuildProperties(prop2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event){
		System.out.println("event:"+event);
		return new TextMessage(event.getMessage().getText());
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

}
