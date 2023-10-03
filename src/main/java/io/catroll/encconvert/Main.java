package io.catroll.encconvert;

import io.catroll.encconvert.ConvertFileEncoding;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        copyDirectory("政府工作报告完全版", "GB2312");
    }
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                        if (destination.toString().contains(".txt")) {
                            System.out.println(destination.toString());
                            ConvertFileEncoding.convert(destination.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}