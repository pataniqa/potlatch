package com.pataniqa.coursera.potlatch.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.pataniqa.coursera.potlatch.server.repository.ServerGift;

public class FileManager {

    private String path;
    private String extension;
    private Path targetDir;

    public FileManager(String path, String extension) {
        this.path = path;
        this.extension = extension;
        targetDir = Paths.get(path);
        if (!Files.exists(targetDir))
            try {
                Files.createDirectories(targetDir);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create file manager directory " + path);
            }
    }

    private Path getPath(ServerGift g) {
        assert (g != null);
        return targetDir.resolve(path + g.getId() + extension);
    }

    public boolean hasData(ServerGift g) {
        return Files.exists(getPath(g));
    }

    public void getData(ServerGift g, OutputStream out) throws IOException {
        Path source = getPath(g);
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Unable to find the referenced file for gift Id:"
                    + g.getId());
        }
        Files.copy(source, out);
    }

    public void saveData(ServerGift g, InputStream giftData) throws IOException {
        assert (giftData != null);
        Files.copy(giftData, getPath(g), StandardCopyOption.REPLACE_EXISTING);
    }
}
