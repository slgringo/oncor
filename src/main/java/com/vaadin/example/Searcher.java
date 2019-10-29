package com.vaadin.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Searcher {
    private static final Pattern PATTERN =  Pattern.compile(".*\\.md");

    public List<String> getMatchesFilenames(String searchText) {
        return getFileListForSearch().stream().filter(f -> fileMathches(f, searchText)).map(File::getName).collect(Collectors.toList());
    }

    private List<File> getFileListForSearch() {
        File folder = null;
        try {
            folder = new File(getClass().getResource("/").toURI().getPath());
        } catch (URISyntaxException e) {

        }

        File [] files = folder.listFiles();
        if (files != null) {
            return Arrays.stream(files).filter(f -> !f.isDirectory() && PATTERN.matcher(f.getName()).matches()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private boolean fileMathches(File file, String searchText) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
            int index = data.lastIndexOf("mkb:");
            if (index > 0) {
                String val = data.substring(index + 4).trim().split("\r")[0];
                if (val.length() > 2) {
                    val = val.substring(1, val.length() - 2);
                    Pattern pattern = Pattern.compile(val, Pattern.CASE_INSENSITIVE);
                    return pattern.matcher(searchText).matches();
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
