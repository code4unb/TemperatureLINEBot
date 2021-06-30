package com.code4unb.TemperatureLINEBot;

import com.linecorp.bot.client.LineMessagingClient;
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

}
