package com.code4unb.TemperatureLINEBot.util;

import com.code4unb.TemperatureLINEBot.model.UserData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputMapping {
    @JsonProperty("form_id")
    private String formId;

    @JsonProperty("mappings")
    @JsonDeserialize(as=LinkedHashSet.class)
    private LinkedHashSet<MappingItem> mappingItems;

    public static final String DIR = "form_mappings";

    static Map<UserData.ClassRoom,InputMapping> mappings;

    static{
        mappings = new HashMap<>();

        Pattern pattern = Pattern.compile("mapping_([0-9])_([0-9]+).json");
        InputMapping.class.getClassLoader().resources(DIR+"/")
                .flatMap(x-> Arrays.stream(new File(x.getFile()).listFiles()).map(y->y.getName()))
                .map(x->pattern.matcher(x))
                .filter(x->x.matches())
                .forEach(x->{
                    UserData.Grades grade = UserData.Grades.values()[ Integer.parseInt(x.group(1))-1];
                    int _class = Integer.parseInt(x.group(2));
                    UserData.ClassRoom classRoom = new UserData.ClassRoom(grade,_class);
                    mappings.put(classRoom,load(classRoom));
                });
    }

    public static Optional<InputMapping> getInstance(UserData.ClassRoom classRoom){
        return Optional.ofNullable(mappings.get(classRoom));
    }

    private static InputMapping load(UserData.ClassRoom classRoom){
        String path = String.format(DIR+"/mapping_%d_%d.json",classRoom.getGrade().ordinal()+1,classRoom.getClass_());

        try (InputStream is = InputMapping.class.getClassLoader().getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder result = new StringBuilder() ;
            String str = br.readLine();
            while(str != null){
                result.append(str);
                str = br.readLine();
            }

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(result.toString(), InputMapping.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MappingItem{
        //entry.xxxxx
        @JsonProperty("input_id")
        String inputId;

        @JsonProperty("value")
        String value;
    }
}
