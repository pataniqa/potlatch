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

import com.pataniqa.coursera.potlatch.model.GetId;

public class FileManager<T extends GetId> {

    private final String path;
    private final String extension;
    private final Path targetDir;

    public FileManager(String path, String extension) {
        this.path = "target" + File.separator + path;
        this.extension = extension;
        targetDir = Paths.get(this.path);
        if (!Files.exists(targetDir))
            try {
                Files.createDirectories(targetDir);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create file manager directory " + this.path);
            }
    }

    private Path getPath(T t) {
        assert (t != null);
        return targetDir.resolve(path + t.getId() + extension);
    }

    public boolean hasData(T t) {
        return Files.exists(getPath(t));
    }

    public void getData(T t, OutputStream out) throws IOException {
        Path source = getPath(t);
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Unable to find the referenced file for Id:"
                    + t.getId());
        }
        Files.copy(source, out);
    }

    public void saveData(T t, InputStream giftData) throws IOException {
        assert (giftData != null);
        Files.copy(giftData, getPath(t), StandardCopyOption.REPLACE_EXISTING);
    }
}
