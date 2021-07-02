package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import com.code4unb.TemperatureLINEBot.db.entity.UserDataEntity;
import com.code4unb.TemperatureLINEBot.message.*;
import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.PostbackReply;
import com.code4unb.TemperatureLINEBot.model.UserData;
import com.code4unb.TemperatureLINEBot.util.FlexMessages;
import com.code4unb.TemperatureLINEBot.util.Forms;
import com.code4unb.TemperatureLINEBot.util.InputMapping;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TemperatureMessageHandler extends FlowMessageHandler {
    @Autowired
    UserDataRepository userDataRepository;

    public TemperatureMessageHandler(){
        super("入力","体温入力","体温","検温入力","検温");
        setHidden(true);
    }

    @Override
    protected List<Flow> initFlows() {
        return Arrays.asList(
                new Flow() {
                    @Override
                    public Optional<List<Message>> postHandle(Source source) {
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("体温を入力してください。").build()));
                    }

                    @Override
                    public FlowResult handle(MessageReply message) {
                        Optional<Float> parsed = parseFloat(message.getKeyPhrase());
                        if (parsed.isPresent() && (33f <= parsed.get() && parsed.get() < 43f)) {
                            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
                            session.addData("measured_data", new MeasurementData(String.format("%.1f", parsed.get()), LocalDate.now(), MeasurementData.TimeConvention.Now()));
                            sessionManager.addSession(message.getSource().getUserId(), session);
                            return new FlowResult(Optional.empty(), true);
                        } else {
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。体温は33℃から43℃の範囲内で入力してください。").build())), false);
                        }
                    }
                },
                new Flow() {

                    @Override
                    public Optional<List<Message>> postHandle(Source source) {
                        QuickReply quickReply = QuickReply.builder()
                                .item(QuickReplyItem.builder().action(new MessageAction("am", "午前")).build())
                                .item(QuickReplyItem.builder().action(new MessageAction("pm", "午後")).build())
                                .build();
                        return Optional.of(Collections.singletonList(TextMessage.builder().text("午前/午後(AM/PM)を入力してください。").quickReply(quickReply).build()));
                    }

                    @Override
                    public FlowResult handle(MessageReply message) {
                        Optional<MeasurementData.TimeConvention> result = MeasurementData.TimeConvention.Parse(message.getKeyPhrase());
                        if (result.isPresent()) {
                            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
                            session.addData("measured_data", ((MeasurementData) session.getData("measured_data")).withConvention(result.get()));
                            sessionManager.addSession(message.getSource().getUserId(), session);
                            return new FlowResult(Optional.empty(), true);
                        } else {
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。もう一度入力してください。").build())), false);
                        }
                    }
                },
                new PostBackFlow() {

                    @Override
                    public FlowResult handlePostback(PostbackReply reply) {
                        switch (reply.getContent().getData()) {
                            case "measure_date":
                                LocalDate date = LocalDate.parse(reply.getContent().getParams().get("date"));
                                Session session = sessionManager.findOrCreateSession(reply.getSource().getUserId());
                                session.addData("measured_data", ((MeasurementData) session.getData("measured_data")).withDate(date));
                                sessionManager.addSession(reply.getSource().getUserId(), session);

                                Optional<UserDataEntity> optional = userDataRepository.findByLineID(reply.getSource().getUserId());
                                if (!optional.isPresent()) {
                                    sessionManager.removeSession(reply.getSource().getUserId());
                                    return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("ユーザー登録がされていません。'登録'と入力して登録手続きを行ってください。").build())), false);
                                }
                                UserData data = optional.get().toUserData();

                                return new FlowResult(Optional.empty(), true);
                        }
                        return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("エラーが発生しました。").build())), false);
                    }

                    @Override
                    public Optional<List<Message>> postHandle(Source source) {
                        QuickReply quickReply = QuickReply.builder()
                                .item(QuickReplyItem.builder().action(new MessageAction("今日", "今日")).build())
                                .item(QuickReplyItem.builder().action(new MessageAction("昨日", "昨日")).build())
                                .item(QuickReplyItem.builder().action(new MessageAction("一昨日", "一昨日")).build())
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
                        try {
                            switch (message.getKeyPhrase()) {
                                case "昨日":
                                    date = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST"))).minusDays(1);
                                    break;
                                case "一昨日":
                                    date = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST"))).minusDays(2);
                                    break;
                                case "今日":
                                    date = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST")));
                                    break;
                                default:
                                    date = LocalDate.parse(LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("JST"))).getYear() + "-" + message.getKeyPhrase());
                                    break;
                            }
                            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
                            session.addData("measured_data", ((MeasurementData) session.getData("measured_data")).withDate(date));
                            sessionManager.addSession(message.getSource().getUserId(), session);
                            return new FlowResult(Optional.empty(), true);
                        } catch (Exception e) {
                            return new FlowResult(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。もう一度入力してください。").build())), false);
                        }
                    }
                },
                new PostBackFlow() {
                    @Override
                    public FlowResult handlePostback(PostbackReply reply) {
                        Session session = sessionManager.findOrCreateSession(reply.getSource().getUserId());
                        MeasurementData data = (MeasurementData) session.getData("measured_data");
                        UserData user = userDataRepository.findByLineID(reply.getSource().getUserId()).get().toUserData();
                        switch(reply.getData()){
                            case "submit":
                                int states = Forms.submit(user, ((MeasurementData) session.getData("measured_data")));
                                if(states==200){
                                    log.info(String.format("Submission successful"));
                                    return FlowResult.builder().succeed(true).result(Optional.of(Collections.singletonList(TextMessage.builder().text("検温を入力しました。").build()))).build();
                                }else{
                                    log.error(String.format("Failed to submit measurement data:[response=%d,query=%s]",states,Forms.getEditableFormUri(user,data).toString()));
                                    return FlowResult.builder().succeed(true).result(Optional.of(Arrays.asList(TextMessage.builder().text("検温の入力に失敗しました。").build(),TextMessage.builder().text(Forms.getEditableFormUri(user,data).toString()).build()))).build();
                                }
                            case "edit":
                                return FlowResult.builder().succeed(true).result(Optional.of(Arrays.asList(TextMessage.builder().text("次のリンクから修正を行ってください。").build(),TextMessage.builder().text(Forms.getEditableFormUri(user,data).toString()).build()))).build();
                            default:
                                return FlowResult.builder().succeed(false).result(Optional.of(Collections.singletonList(TextMessage.builder().text("不正な操作が行われました。").build()))).build();
                        }
                    }

                    @Override
                    public Optional<List<Message>> postHandle(Source source) {
                        Session session = sessionManager.findOrCreateSession(source.getUserId());
                        MeasurementData data = (MeasurementData) session.getData("measured_data");
                        UserData user = userDataRepository.findByLineID(source.getUserId()).get().toUserData();
                        if(InputMapping.getInstance(user.getClassRoom()).get().isOauthRequired()){
                            FlexContainer container = FlexMessages.LoadContainerFromJson(FlexMessages.LoadJsonFromJsonFile("oauth-required.json").replace("%uri",Forms.getEditableFormUri(user,data).toString()));
                            sessionManager.removeSession(source.getUserId());
                            return Optional.of(Collections.singletonList(FlexMessage.builder().altText("URL").contents(container).build()));
                        }else{
                            return Optional.of(Collections.singletonList(FlexMessages.CreateConfirmSubmitMessage(user,data)));
                        }
                    }

                    @Override
                    public FlowResult handle(MessageReply message) {
                        return null;
                    }
                }
        );
    }

    @Override
    protected List<Message> handleActivateMessage(MessageReply message) {
        Optional<UserDataEntity> optional =  userDataRepository.findByLineID(message.getSource().getUserId());
        if(!optional.isPresent()){
            sessionManager.removeSession(message.getSource().getUserId());
            return Collections.singletonList(TextMessage.builder().text("ユーザー登録がされていません。'登録'と入力して登録手続きを行ってください。").build());
        }
        UserData data = optional.get().toUserData();

        if(!Forms.isAvailableClassroom(data.getClassRoom())){
            sessionManager.removeSession(message.getSource().getUserId());
            return Collections.singletonList(TextMessage.builder().text("あなたのホームルームは現在非対応です。開発者まで連絡をいただければあなたのホームルームに対応することが可能です。").build());
        }

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
