package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.message.*;
import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.PostbackReply;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class TemperatureMessageHandler extends FlowMessageHandler {
    @Autowired
    LineMessagingClient client;

    public TemperatureMessageHandler(){
        super("入力","体温入力","体温","検温入力","検温");
        setHidden(true);
    }

    @Override
    protected List<Flow> initFlows() {
        return Arrays.asList(
                new Flow() {
                    @Override
                    public Optional<List<Message>> postHandle() {
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("体温を入力してください。").build()));
                    }

                    @Override
                    public FlowResult handle(MessageReply message) {
                        Optional<Float> parsed = parseFloat(message.getKeyPhrase());
                        if(parsed.isPresent() && (33f <= parsed.get() && parsed.get() < 43f)){
                            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
                            session.addData("measured_data",new MeasurementData(String.format("%.1f", parsed.get()), LocalDate.now(), MeasurementData.TimeConvention.Now()));
                            sessionManager.addSession(message.getSource().getUserId(),session);
                            return new FlowResult(Optional.empty(),true);
                        }else{
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。体温は33℃から43℃の範囲内で入力してください。").build())),false);
                        }
                    }
                },
                new Flow(){

                    @Override
                    public Optional<List<Message>> postHandle() {
                        QuickReply quickReply = QuickReply.builder()
                                .item(QuickReplyItem.builder().action(new MessageAction("am","午前")).build())
                                .item(QuickReplyItem.builder().action(new MessageAction("pm","午後")).build())
                                .build();
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("午前/午後(AM/PM)を入力してください。").quickReply(quickReply).build()));
                    }

                    @Override
                    public FlowResult handle(MessageReply message) {
                        Optional<MeasurementData.TimeConvention> result = MeasurementData.TimeConvention.Parse(message.getKeyPhrase());
                        if(result.isPresent()){
                            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
                            session.addData("measured_data",((MeasurementData)session.getData("measured_data")).withConvention(result.get()));
                            sessionManager.addSession(message.getSource().getUserId(),session);
                            return new FlowResult(Optional.empty(),true);
                        }else{
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。もう一度入力してください。").build())),false);
                        }
                    }
                },
                new PostBackFlow(){

                    @Override
                    public FlowResult handlePostback(PostbackReply reply) {
                        switch(reply.getContent().getData()){
                            case "measure_date":
                                LocalDate date = LocalDate.parse(reply.getContent().getParams().get("date"));
                                Session session = sessionManager.findOrCreateSession(reply.getSource().getUserId());
                                session.addData("measured_data",((MeasurementData)session.getData("measured_data")).withDate(date));
                                sessionManager.addSession(reply.getSource().getUserId(),session);
                                return FlowResult.builder()
                                        .result(Optional.of(Collections.singletonList(TextMessage.builder().text(((MeasurementData)session.getData("measured_data")).toString()).build())))
                                        .succeed(true)
                                        .build();
                        }
                        return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("エラーが発生しました。").build())),false);
                    }

                    @Override
                    public Optional<List<Message>> postHandle() {
                        QuickReply quickReply = QuickReply.builder()
                                .item(QuickReplyItem.builder().action(new MessageAction("today","今日")).build())
                                .item(QuickReplyItem.builder().action(new MessageAction("1day","昨日")).build())
                                .item(QuickReplyItem.builder().action(new MessageAction("2day","一昨日")).build())
                                .item(QuickReplyItem.builder().action(new DatetimePickerAction<LocalDate>() {
                                    @Override
                                    public String getLabel() {
                                        return "選択";
                                    }

                                    @Override
                                    public String getData() {
                                        return "measure_date";
                                    }

                                    @Override
                                    public Mode getMode() {
                                        return Mode.DATE;
                                    }

                                    @Override
                                    public LocalDate getInitial() {
                                        return LocalDate.now();
                                    }

                                    @Override
                                    public LocalDate getMax() {
                                        return LocalDate.now();
                                    }

                                    @Override
                                    public LocalDate getMin() {
                                        return null;
                                    }
                                }).build())
                                .build();
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("日付を入力してください。'今日' '昨日' '一昨日' もしくは直接日付を入力できます。例)6月29日→06-29").quickReply(quickReply).build()));
                    }

                    @Override
                    public FlowResult handle(MessageReply message) {
                        LocalDate date;
                        try{
                            switch(message.getKeyPhrase()){
                                case "昨日":
                                    date = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST"))).minusDays(1);
                                    break;
                                case "一昨日":
                                    date= LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST"))).minusDays(2);
                                    break;
                                case "今日":
                                    date = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST")));
                                    break;
                                default:
                                    date = LocalDate.parse(LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST"))).getYear()+"-"+message.getKeyPhrase());
                                    break;
                            }
                            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
                            session.addData("measured_data",((MeasurementData)session.getData("measured_data")).withDate(date));
                            sessionManager.addSession(message.getSource().getUserId(),session);
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text(((MeasurementData)session.getData("measured_data")).toString()).build())),true);
                        }catch (Exception e){
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。もう一度入力してください。").build())),false);
                        }
                    }
                }
        );
    }

    @Override
    protected List<Message> handleActivateMessage(MessageReply message) {
        return null;
    }

    private Optional<Float> parseFloat(String text){
        try{
            return Optional.of(Float.parseFloat(text));
        }catch(Exception e){
            return Optional.empty();
        }
    }
}
