package com.code4unb.TemperatureLINEBot.util;

import com.code4unb.TemperatureLINEBot.db.entity.UserData;
import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Forms {
    public static final String REQUEST_URL = "https://docs.google.com/forms/d/%s/";
    public static final String ACCOUNT_CHOOSER_URL = "https://accounts.google.com/AccountChooser/signinchooser?continue=%s&service=wise&flowName=GlifWebSignIn&flowEntry=AccountChooser";

    public static int submit(UserData user, MeasurementData data){
        Optional<InputMapping> optionalMapping =  InputMapping.getInstance(user.getClassRoom());
        if(!optionalMapping.isPresent())return -1;
        InputMapping mapping = optionalMapping.get();

        HttpClient client = HttpClient.newHttpClient();

        try {
            URL url = getSubmitFormUri(user,data).toURL();
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int res = connection.getResponseCode();
            connection.disconnect();

            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static URI getEditableFormUri(UserData user,MeasurementData data){
        return getEditableFormUri(user,data,false);
    }
    public static URI getEditableFormUri(UserData user,MeasurementData data,boolean openExternalBrowser){
        Optional<InputMapping> optionalMapping =  InputMapping.getInstance(user.getClassRoom());
        if(!optionalMapping.isPresent())return null;
        InputMapping mapping = optionalMapping.get();
        String path = String.format(REQUEST_URL+"viewform",mapping.getFormId());
        String param = createParams(mapping,user,data).stream().map(x->String.format("entry.%s=%s",x.entry,URLEncoder.encode(x.value,StandardCharsets.UTF_8))).collect(Collectors.joining("&"));
        if(openExternalBrowser){
            param += "&openExternalBrowser=1";
        }

        return URI.create(path +"?"+param);
    }

    public static URI getSubmitFormUri(UserData user,MeasurementData data){
        return getSubmitFormUri(user,data,false);
    }
    public static URI getSubmitFormUri(UserData user,MeasurementData data,boolean openExternalBrowser){
        Optional<InputMapping> optionalMapping =  InputMapping.getInstance(user.getClassRoom());
        if(!optionalMapping.isPresent())return null;
        InputMapping mapping = optionalMapping.get();
        String path = String.format(REQUEST_URL+"formResponse",mapping.getFormId());
        String param = createParams(mapping,user,data).stream().map(x->String.format("entry.%s=%s",x.entry,URLEncoder.encode(x.value,StandardCharsets.UTF_8))).collect(Collectors.joining("&"));
        if(openExternalBrowser){
            param += "&openExternalBrowser=1";
        }

        return URI.create(path +"?"+param);
    }

    public static URI getAccountChooserURi(UserData user,MeasurementData data){
        return getAccountChooserUri(user,data,false);
    }
    public static URI getAccountChooserUri(UserData user,MeasurementData data,boolean openExternalBrowser){
        String uri = String.format(ACCOUNT_CHOOSER_URL, URLEncoder.encode(getSubmitFormUri(user,data).toString(),StandardCharsets.UTF_8));
        if(openExternalBrowser){
            uri += "&openExternalBrowser=1";
        }
        return URI.create(uri);
    }

    public static Set<FormsParam> createParams(InputMapping mapping, UserData user, MeasurementData data){
        return mapping.getMappingItems().stream().map(x->new FormsParam(
                x.inputId,
                x.name,
                x.value
                        .replace(InputMapping.Replacer.DATE.toString(),data.getDate().toString())
                        .replace(InputMapping.Replacer.TIMECONVENTION.toString(), Objects.toString((data.getConvention() == MeasurementData.TimeConvention.AM ? x.getChoices().get("am") : x.getChoices().get("pm"))))
                        .replace(InputMapping.Replacer.NUMBER.toString(),String.valueOf(user.getNumber()))
                        .replace(InputMapping.Replacer.NAME.toString(),user.getName())
                        .replace(InputMapping.Replacer.TEMPERATURE.toString(),data.getTemperature())
        )).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static boolean isAvailableClassroom(UserData.ClassRoom classRoom){
        return InputMapping.getInstance(classRoom).isPresent();
    }

    @Data
    @AllArgsConstructor
    public static class FormsParam{
        private String entry;

        private String name;

        private String value;
    }
}
