package dev.shylisaa.firespigot.api.extension;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public record Extension(String main, String name, String version, String[] authors, String description, String fileName) {

    public static Extension fromJson(InputStream inputStream, File file) {
        try {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();

            return new Extension(jsonObject.get("main").getAsString(), jsonObject.get("name").getAsString(), jsonObject.get("version").getAsString(), new Gson().fromJson(jsonObject.get("authors").getAsJsonArray(), String[].class), jsonObject.get("description").getAsString(), file.getName());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
