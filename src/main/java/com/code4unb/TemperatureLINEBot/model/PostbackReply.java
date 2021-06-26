package com.code4unb.TemperatureLINEBot.model;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.event.source.Source;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostbackReply extends Reply<PostbackContent>{
    String data;

    Map<String,String> params;

    public PostbackReply(Instant timestamp, Source source, PostbackContent content){
        super(timestamp,source,content);
        data = content.getData();
        params = content.getParams();
    }

    public static PostbackReply Build(PostbackEvent event){
        return new PostbackReply(event.getTimestamp(),event.getSource(),event.getPostbackContent());
    }
}
