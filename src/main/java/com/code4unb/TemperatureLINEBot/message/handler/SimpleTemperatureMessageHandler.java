package com.code4unb.TemperatureLINEBot.message.handler;

import com.code4unb.TemperatureLINEBot.db.SubmissionDataRepository;
import com.code4unb.TemperatureLINEBot.db.UserDataRepository;
import com.code4unb.TemperatureLINEBot.db.entity.SubmissionData;
import com.code4unb.TemperatureLINEBot.db.entity.UserData;
import com.code4unb.TemperatureLINEBot.message.*;
import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.MessageReply;
import com.code4unb.TemperatureLINEBot.model.PostbackReply;
import com.code4unb.TemperatureLINEBot.util.FlexMessages;
import com.code4unb.TemperatureLINEBot.util.Forms;
import com.code4unb.TemperatureLINEBot.util.InputMapping;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SimpleTemperatureMessageHandler extends FlowMessageHandler {
    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    SubmissionDataRepository submissionDataRepository;

    public SimpleTemperatureMessageHandler(){
        super("簡単検温");
        setHidden(true);
    }

    @Override
    public boolean shouldHandle(String phrase){
        return parseTemperature(phrase).isPresent();
    }

    @Override
    protected List<Flow> initFlows() {
        return Arrays.asList(
            new PostBackFlow(){

                @Override
                public FlowResult handlePostback(PostbackReply reply) {
                    Session session = sessionManager.findOrCreateSession(reply.getSource().getUserId());
                    MeasurementData data = (MeasurementData) session.getData("measured_data");
                    UserData user = userDataRepository.findByLineID(reply.getSource().getUserId()).get();
                    switch(reply.getData()){
                        case "submit":
                            int states = Forms.submit(user, ((MeasurementData) session.getData("measured_data")));
                            submissionDataRepository.save(user.getId(), Timestamp.from(Instant.now()),Forms.createQuery(Forms.createParams(InputMapping.getInstance(user.getClassRoom()).get(),user,data)), SubmissionData.CommandType.SIMPLE,String.valueOf(states));
                            if(states==200){
                                log.info("Submission successful");
                                return FlowResult.builder().succeed(true).singletonResult(TextMessage.builder().text("検温を入力しました。").build()).build();
                            }else{
                                log.error(String.format("Failed to submit measurement data:[response=%d,query=%s]",states,Forms.getEditableFormUri(user,data).toString()));
                                return FlowResult.builder().succeed(true).result(Arrays.asList(TextMessage.builder().text("検温の入力に失敗しました。").build(),TextMessage.builder().text(Forms.getEditableFormUri(user,data).toString()).build())).build();
                            }
                        case "edit":
                            submissionDataRepository.save(user.getId(), Timestamp.from(Instant.now()),Forms.createQuery(Forms.createParams(InputMapping.getInstance(user.getClassRoom()).get(),user,data)), SubmissionData.CommandType.NORMAL,"edit");
                            return FlowResult.builder().succeed(true).result(Arrays.asList(TextMessage.builder().text("次のリンクから修正を行い、ブラウザから送信してください。。※上の送信ボタンは動作しません。").build(),TextMessage.builder().text(Forms.getEditableFormUri(user,data).toString()).build())).build();
                        default:
                            return FlowResult.builder().succeed(false).singletonResult(TextMessage.builder().text("不正な操作が行われました。").build()).build();
                    }
                }

                @Override
                public Optional<List<Message>> postHandle(Source source) {
                    Session session = sessionManager.findOrCreateSession(source.getUserId());
                    MeasurementData data = (MeasurementData) session.getData("measured_data");
                    UserData user = userDataRepository.findByLineID(source.getUserId()).get();
                    if(InputMapping.getInstance(user.getClassRoom()).get().isOauthRequired()){
                        FlexContainer container = FlexMessages.LoadContainerFromJson(FlexMessages.LoadJsonFromJsonFile("oauth-required")
                                .replace("%uri1",Forms.getSubmitFormUri(user,data,true).toString())
                                .replace("%uri2",Forms.getAccountChooserUri(user,data,true).toString())
                        );
                        sessionManager.removeSession(source.getUserId());
                        submissionDataRepository.save(user.getId(), Timestamp.from(Instant.now()),Forms.createQuery(Forms.createParams(InputMapping.getInstance(user.getClassRoom()).get(),user,data)), SubmissionData.CommandType.NORMAL,"oauth");
                        return Optional.of(Collections.singletonList(FlexMessage.builder().altText("URL").contents(container).build()));
                    }else{
                        return Optional.of(Collections.singletonList(FlexMessages.CreateConfirmSubmitMessage(user,data)));
                    }
                }

                @Override
                public FlowResult handle(MessageReply message) {
                    return FlowResult.EMPTY_FAILED;
                }
            }
        );
    }

    @Override
    protected List<Message> handleActivateMessage(MessageReply message) {
        Optional<UserData> optional =  userDataRepository.findByLineID(message.getSource().getUserId());
        if(!optional.isPresent()){
            sessionManager.removeSession(message.getSource().getUserId());
            return Collections.singletonList(TextMessage.builder().text("ユーザー登録がされていません。'登録'と入力して登録手続きを行ってください。").build());
        }
        UserData userData = optional.get();

        if(!Forms.isAvailableClassroom(userData.getClassRoom())){
            sessionManager.removeSession(message.getSource().getUserId());
            return Collections.singletonList(TextMessage.builder().text("あなたのホームルームは現在非対応です。開発者まで連絡をいただければあなたのホームルームに対応することが可能です。").build());
        }

        Optional<Float> parsed = parseTemperature(message.getKeyPhrase());
        if (parsed.isPresent()) {
            MeasurementData data = new MeasurementData(String.valueOf(parsed.get()),LocalDate.now(), MeasurementData.TimeConvention.Now());
            for(String arg : message.getArgs()){
                Optional<MeasurementData.TimeConvention> optionalTimeConvention =  MeasurementData.TimeConvention.Parse(arg);
                if(optionalTimeConvention.isPresent()){
                    data = data.withConvention(optionalTimeConvention.get());
                }

                try{
                    LocalDate date = LocalDate.parse(LocalDate.now().getYear() + "-" + arg);
                    data = data.withDate(date);
                }catch (DateTimeParseException e){
                }
            }
            Session session = sessionManager.findOrCreateSession(message.getSource().getUserId());
            session.addData("measured_data", data);

            return null;
        }else {
            sessionManager.removeSession(message.getSource().getUserId());
            return Collections.singletonList(TextMessage.builder().text("不正な入力値 " + message.getKeyPhrase() + " です。体温は33℃から43℃の範囲内で入力してください。").build());
        }
    }

    private Optional<Float> parseTemperature(String text){
        try{
            text = text.replace(",",".").replace("、",".");
            float f = Float.parseFloat(text);
            if(330f <= f && f < 430f){
                f /= 10;
            }
            if(33f <= f && f < 43f){
                return Optional.of(f);
            }else{
                return Optional.empty();
            }
        }catch(Exception e){
            return Optional.empty();
        }
    }
}
