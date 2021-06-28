package com.code4unb.TemperatureLINEBot.util;

import com.code4unb.TemperatureLINEBot.model.UserData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    static {
        mappings = new HashMap<>();

        URI uri = null;

         try {
            uri = InputMapping.class.getClassLoader().getResource(DIR).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile("mapping_([0-9])_([0-9]+).json");

        if(uri.getScheme().contains("jar")){
            CodeSource src = InputMapping.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = null;
                try {
                    zip = new ZipInputStream(jar.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while(true) {
                    ZipEntry e = null;
                    try {
                        e = zip.getNextEntry();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith("BOOT-INF/classes/"+DIR)) {
                        String file = name.replace("BOOT-INF/classes/"+DIR+"/","");
                        Matcher match = pattern.matcher(file);
                        if(match.matches()){
                            UserData.Grades grade = UserData.Grades.values()[ Integer.parseInt(match.group(1))-1];
                            int _class = Integer.parseInt(match.group(2));
                            UserData.ClassRoom classRoom = new UserData.ClassRoom(grade,_class);
                            mappings.put(classRoom,load(classRoom));
                        }
                    }
                }
            }
        }
        else{
            InputMapping.class.getClassLoader().resources(DIR)
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
    }

    public static Optional<InputMapping> getInstance(UserData.ClassRoom classRoom){
        return Optional.ofNullable(mappings.get(classRoom));
    }

    public static Set<UserData.ClassRoom> getAllClassrooms(){
        return mappings.keySet();
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

        @JsonProperty("name")
        String name;

        @JsonProperty("value")
        String value;
    }
}
