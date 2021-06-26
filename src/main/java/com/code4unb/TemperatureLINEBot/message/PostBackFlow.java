package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.PostbackReply;

public abstract class PostBackFlow extends Flow{
    public abstract FlowResult handlePostback(PostbackReply reply);
}
