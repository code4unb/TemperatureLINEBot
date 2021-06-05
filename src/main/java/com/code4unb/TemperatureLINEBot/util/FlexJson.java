package com.code4unb.TemperatureLINEBot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.message.flex.container.FlexContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FlexJson {
    public static FlexContainer LoadFlexContainer(String name){
        return CreateFlexContainer(LoadMessageJson(name));
    }

    public static FlexContainer CreateFlexContainer(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, FlexContainer.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String LoadMessageJson(String name){
        String path = "messages/"+name+".json";
        try (InputStream is = FlexJson.class.getClassLoader().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder() ;
            String str = br.readLine();
            while(str != null){
                result.append(str);
                str = br.readLine();
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
