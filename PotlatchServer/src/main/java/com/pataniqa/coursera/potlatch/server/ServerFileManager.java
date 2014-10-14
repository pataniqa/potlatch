package com.pataniqa.coursera.potlatch.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;

public class ServerFileManager {

    private static Logger log = Logger.getLogger(ServerFileManager.class.getName());

    private static Path getPath(String dirpath, String extension, long id) {
        String path = "target" + File.separator + dirpath;
        Path targetDir = Paths.get(path);
        if (!Files.exists(targetDir))
            try {
                Files.createDirectories(targetDir);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create file manager directory " + dirpath);
            }
        return targetDir.resolve(dirpath + id + "." + extension);
    }

    public static boolean hasData(String dirpath, String extension, long id) {
        return Files.exists(getPath(dirpath, extension, id));
    }

    public static void getData(String dirpath, String extension, long id, OutputStream out)
            throws IOException {
        Path source = getPath(dirpath, extension, id);
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Unable to find the referenced file for Id:" + id);
        }
        Files.copy(source, out);
    }

    public static void saveData(String dirpath, String extension, long id, InputStream giftData)
            throws IOException {
        Path path = getPath(dirpath, extension, id);
        log.info("Writing file to " + path.toString());
        System.out.println("Writing file to " + path.toString());
        Files.copy(giftData, path, StandardCopyOption.REPLACE_EXISTING);
    }
}
