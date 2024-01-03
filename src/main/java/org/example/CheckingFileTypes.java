package org.example;

import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class CheckingFileTypes {
    public static void main(String[] args) {
        String filetype = "Unknown";
        File  figtree = new File("C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\free-label-important-1779593-1512505.png");
        try {
            FileInputStream ffi = new FileInputStream(figtree);
            filetype = new Tika().detect(ffi);
            System.out.println(filetype);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
