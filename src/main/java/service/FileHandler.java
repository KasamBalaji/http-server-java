package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {

    public static String getFileAsString(String directory, String filePath) throws IOException {

        Path path = Paths.get(directory,filePath).toAbsolutePath();
        File file = new File(path.toString());
        if(!file.exists()){
            throw new FileNotFoundException(String.format("File %s doesn't exist", path.toString()));
        }
        return Files.readString(path);
    }

    public static void writeFile(String directory, String filePath, String content) throws IOException {

        Path path = Paths.get(directory,filePath).toAbsolutePath();
        File file = new File(path.toString());
        if(!file.exists())
            Files.createFile(path);
        Files.writeString(path, content);


    }


}
