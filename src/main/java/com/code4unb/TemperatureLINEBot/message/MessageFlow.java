package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
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

    public List<Message> handleNextFlow(ReceivedMessage message){
        if(isCompleted()) return null;
        FlowResult result = flows.get(currentIndex).handle(message);
        if(result.isSucceed()){
            List<Message> resultMessage = new ArrayList<>();

            result.getResult().ifPresent(resultMessage::addAll);
            getNextFlow().flatMap(Flow::postHandle).ifPresent(resultMessage::addAll);

            currentIndex++;

            return resultMessage;
        }else{
            return result.getResult().get();
        }
    }

}
