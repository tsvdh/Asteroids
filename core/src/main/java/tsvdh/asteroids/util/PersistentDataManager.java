package tsvdh.asteroids.util;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersistentDataManager {

    private final Path filePath;
    private final ObjectWriter objectWriter;

    public final Data data;

    public PersistentDataManager(String fileName) {
        this.filePath = Paths.get(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer(SerializationFeature.INDENT_OUTPUT);

        if (!Files.exists(filePath)) {
            data = new Data();
            try {
                Files.createFile(filePath);
                objectWriter.writeValue(filePath, data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            data = objectMapper.readValue(filePath, Data.class);
        }
    }

    public void write() {
        try {
            objectWriter.writeValue(filePath, data);
        } catch (JacksonException e) {
            System.out.printf("Writing data failed: %s", e.getMessage());
        }
    }

    public static class Data {
        public int classicScore;
        public int towerDefenseScore;
    }
}
