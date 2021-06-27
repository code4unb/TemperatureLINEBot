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

@SpringBootApplication
public class TemperatureLineBotApplication {
	@Value("${line.bot.channel-token}")
	private String token;

	private static LineMessagingClient client;

	public static void main(String[] args) {
		SpringApplication.run(TemperatureLineBotApplication.class, args);
		initialize();
	}

	private static void initialize(){
		//client = LineMessagingClient.builder(token).build();
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
