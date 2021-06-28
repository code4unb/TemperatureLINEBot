package com.code4unb.TemperatureLINEBot.util;

import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.UserData;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Forms {
    public static final String REQUEST_URL = "https://docs.google.com/forms/d/e/%s/";

    public static int submit(UserData user, MeasurementData data){
        Optional<InputMapping> optionalMapping =  InputMapping.getInstance(user.getClassRoom());
        if(!optionalMapping.isPresent())return -1;
        InputMapping mapping = optionalMapping.get();

        String path = String.format(REQUEST_URL+"formResponse",mapping.getFormId());
        String param = createParams(mapping,user,data).stream().map(x->String.format("entry.%s=%s",x.entry,URLEncoder.encode(x.value,StandardCharsets.UTF_8))).collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();

        try {
            URL url = URI.create(path +"?"+param).toURL();
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
        Optional<InputMapping> optionalMapping =  InputMapping.getInstance(user.getClassRoom());
        if(!optionalMapping.isPresent())return null;
        InputMapping mapping = optionalMapping.get();
        String path = String.format(REQUEST_URL+"viewform",mapping.getFormId());
        String param = createParams(mapping,user,data).stream().map(x->String.format("entry.%s=%s",x.entry,URLEncoder.encode(x.value,StandardCharsets.UTF_8))).collect(Collectors.joining("&"));

        return URI.create(path +"?"+param);
    }

    public static Set<FormsParam> createParams(InputMapping mapping, UserData user, MeasurementData data){
        return mapping.getMappingItems().stream().map(x->new FormsParam(
                x.inputId,
                x.name,
                x.value
                        .replace("%date",data.getDate().toString())
                        .replace("%time_convention",data.getConvention()==MeasurementData.TimeConvention.AM?"午前9:00":"午後16:00")
                        .replace("%number",String.valueOf(user.getNumber()))
                        .replace("%name",user.getName())
                        .replace("%temperature",data.getTemperature())
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
