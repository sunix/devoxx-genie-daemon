package com.devoxx.genie.daemon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.devoxx.genie.FileAdapter;

public class JavaFileAdapter implements FileAdapter{
    private File file;

    public JavaFileAdapter(File file) {
        this.file = file;
    }

    @Override
    public <T> T getAdaptedInstance(Class<T> adaptedClass) {
        if (adaptedClass == File.class) {
            return adaptedClass.cast(file);
        }
        return null;
    }

    @Override
    public String getContent() {
        // try {
        //     return Files.readString(file.toPath());
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     return null;
        // }

        //return complete file path
        return file.getAbsolutePath();
    }

    @Override
    public String getDocumentContent() {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFileType() {
        // if unknown, return "UNKNOWN"
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        if (extension.equals(file.getName())) {
            return "UNKNOWN";
        }
        return extension;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getSelectedText() {
        throw new UnsupportedOperationException("Unimplemented method 'getSelectedText'");
    }


}