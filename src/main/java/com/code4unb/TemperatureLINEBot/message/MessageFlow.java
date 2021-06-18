package com.code4unb.TemperatureLINEBot.message;

import com.code4unb.TemperatureLINEBot.model.ReceivedMessage;
import com.linecorp.bot.model.message.Message;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class MessageFlow {
    private LinkedList<Flow> flows;

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

    private Flow getNextFlow(){
        if(currentIndex+1<=flows.size()){
            return flows.get(currentIndex+1);
        }else{
            return null;
        }
    }

    public List<Message> handleNextFlow(ReceivedMessage message){
        if(isCompleted()) return null;
        FlowResult result = flows.get(currentIndex).handle(message);
        if(result.isSucceed()){
            currentIndex++;

            List<Message> resultMessage = result.getResult();
            resultMessage.addAll(getNextFlow().postHandle());
            return resultMessage;
        }else{
            return result.getResult();
        }
    }

}
