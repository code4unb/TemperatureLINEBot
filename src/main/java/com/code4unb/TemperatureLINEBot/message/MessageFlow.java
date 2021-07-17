package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.PostbackReply;
import com.code4unb.TemperatureLINEBot.model.Reply;
import com.linecorp.bot.model.message.Message;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageFlow {
    private final List<Flow> flows;

    @Getter
    private int currentIndex;

    public void addFlow(Flow flow){
        flows.add(flow);
    }

    public void removeFlow(Flow flow){
        flows.remove(flow);
    }

    public void removeFlow(int index){
        flows.remove(index);
    }

    public void skipThisFlow(){
        currentIndex++;
    }

    public boolean isCompleted(){
        return currentIndex+1>flows.size();
    }

    public MessageFlow(@NonNull List<Flow> flows){
        this.flows = flows;
        init();
    }

    public void init(){
        currentIndex=0;
    }

    public Flow getCurrentFlow(){
        return flows.get(currentIndex);
    }

    public Optional<Flow> getNextFlow(){
        if(currentIndex+1<flows.size()){
            return Optional.of(flows.get(currentIndex+1));
        }else{
            return Optional.empty();
        }
    }

    public List<Message> handleNextFlow(Reply reply){
        if(isCompleted()) return null;
        FlowResult result;
        if(reply instanceof MessageReply){
            result = getCurrentFlow().handle((MessageReply) reply);
        }else if(reply instanceof PostbackReply && getCurrentFlow() instanceof PostBackFlow){
            result = ((PostBackFlow)getCurrentFlow()).handlePostback((PostbackReply) reply);
        }else{
            return null;
        }

        if(result.isSucceed()){
            List<Message> resultMessage = new ArrayList<>();

            result.getResult().ifPresent(resultMessage::addAll);
            getNextFlow().flatMap(flow -> flow.postHandle(reply.getSource())).ifPresent(resultMessage::addAll);

            currentIndex++;

            return resultMessage;
        }else{
            return result.getResult().orElse(null);
        }
    }

}
