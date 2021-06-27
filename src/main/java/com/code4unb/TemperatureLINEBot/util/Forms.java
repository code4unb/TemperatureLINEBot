package com.code4unb.TemperatureLINEBot.util;

import com.code4unb.TemperatureLINEBot.model.MeasurementData;
import com.code4unb.TemperatureLINEBot.model.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Forms {
    public static final String REQUEST_URL = "https://www.docs.google.com/forms/d/e/%s/formResponse";

    public static int submit(UserData user, MeasurementData data){
        Optional<InputMapping> optionalMapping =  InputMapping.getInstance(user.getClassRoom());
        if(!optionalMapping.isPresent())return -1;
        InputMapping mapping = optionalMapping.get();

        String path = String.format(REQUEST_URL,mapping.getFormId());
        String param = createParams(mapping,user,data).stream().map(x->String.format("entry.%s=%s",x.entry,x.value)).collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();

        try {
            URI uri = URI.create(URLEncoder.encode(path +"?"+param, "UTF-8"));
            URL url = new URL(REQUEST_URL+"?"+URLEncoder.encode(param,StandardCharsets.UTF_8));
            System.out.println("URI:"+url.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int res = connection.getResponseCode();
            connection.disconnect();

            if(res!=200){
                BufferedReader br
                        = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                System.out.println(sb.toString());

                br.close();
            }

            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Set<FormsParam> createParams(InputMapping mapping, UserData user, MeasurementData data){
        return mapping.getMappingItems().stream().map(x->new FormsParam(
                x.inputId,
                x.value
                        .replace("%date",data.getDate().toString())
                        .replace("%time_convention",data.getConvention()==MeasurementData.TimeConvention.AM?"午前9:00":"午後16:00")
                        .replace("%number",String.valueOf(user.getNumber()))
                        .replace("%name",user.getName())
                        .replace("%temperature",data.getTemperature())
        )).collect(Collectors.toSet());
    }

    public static boolean isAvailableClassroom(UserData.ClassRoom classRoom){
        return InputMapping.getInstance(classRoom).isPresent();
    }

    @Data
    @AllArgsConstructor
    public static class FormsParam{
        private String entry;

        private String value;
    }
}
