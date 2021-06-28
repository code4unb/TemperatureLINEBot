package com.code4unb.TemperatureLINEBot.util;

import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlexMessages {
    public static FlexContainer LoadContainerFromJsonFile(String name){
        return LoadContainerFromJson(LoadJsonFromJsonFile(name));
    }

    public static FlexContainer LoadContainerFromJson(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, FlexContainer.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String LoadJsonFromJsonFile(String name){
        String path = "messages/"+name+".json";
        try (InputStream is = FlexMessages.class.getClassLoader().getResourceAsStream(path);
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

    public static FlexMessage CreateConfirmSubmitMessage(UserData user, MeasurementData data){
        Text.TextBuilder key = Text.builder()
                .size("sm")
                .color("#555555")
                .flex(0);
        Text.TextBuilder value = Text.builder()
                .size("sm")
                .color("#111111")
                .align(FlexAlign.END);


        List<FlexComponent> inner_box = Forms.createParams(InputMapping.getInstance(user.getClassRoom()).get(),user,data).stream().map(x->Box.builder().layout(FlexLayout.HORIZONTAL).contents(key.text(x.getName()).build(),value.text(x.getValue()).build()).build()).collect(Collectors.toList());

        URI uri = Forms.getEditableFormUri(user,data);

        List<FlexComponent> contents = new ArrayList();
        contents.add(Text.builder().text("入力情報の確認")
                .weight(Text.TextWeight.BOLD)
                .size("xxl")
                .margin("md")
                .align(FlexAlign.CENTER)
                .build());
        contents.add(Separator.builder().margin("xxl").build());
        contents.addAll(inner_box);
        contents.add(Separator.builder().margin("xxl").build());
        contents.add(Box.builder().layout(FlexLayout.VERTICAL).contents(
                Button.builder().action(PostbackAction.builder().label("送信").data("submit").build()).build(),
                Button.builder().action(PostbackAction.builder().label("修正").data("edit").build()).build()
        ).build());

        Box outer_box = Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(contents)
                .build();
        FlexContainer container = Bubble.builder().body(outer_box).build();
        return FlexMessage.builder().contents(container).altText("confirm").build();
    }
}
