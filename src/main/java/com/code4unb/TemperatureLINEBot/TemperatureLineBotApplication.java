package com.code4unb.TemperatureLINEBot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientImpl;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.spring.boot.LineBotAutoConfiguration;
import com.linecorp.bot.spring.boot.LineBotProperties;
import com.linecorp.bot.spring.boot.LineBotWebMvcBeans;
import com.linecorp.bot.spring.boot.LineBotWebMvcConfigurer;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sound.sampled.Line;

@SpringBootApplication
public class TemperatureLineBotApplication {
	@Value("${line.bot.channel-token}")
	private static String token;
	private static LineMessagingClient client;
	public static void main(String[] args) {
		SpringApplication.run(TemperatureLineBotApplication.class, args);
	}
	private static void initialize(){
		client = LineMessagingClient.builder(token).build();
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
