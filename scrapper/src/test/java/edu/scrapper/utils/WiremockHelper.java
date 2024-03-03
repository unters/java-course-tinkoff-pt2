package edu.scrapper.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class WiremockHelper {

    private static final String PROJECT_FOLDER = System.getProperty("user.dir");
    private static final String STUBS_FOLDER = "src/test/resources/wiremock";

    private final String folderName;

    private WiremockHelper(String folderName) {
        this.folderName = folderName;
    }

    public static WiremockHelper getWiremockHelperFor(String folderName) {
        return new WiremockHelper(folderName);
    }

    @SneakyThrows(IOException.class)
    public String getStubForUrlPath(String urlPath) {
        String fileName = urlPath.substring(1).replaceAll("/", "-").toLowerCase() + ".json";
        Path path = Paths.get(PROJECT_FOLDER, STUBS_FOLDER, folderName, fileName);
        String contents = Files.readAllLines(path).stream()
            .collect(Collectors.joining());
        return contents;
    }
}
