package com.mytask.front.service;

import java.io.File;
import java.io.IOException;

public class ExportService {
    public static void createDirectoryIfNotExist(String filepath) {
        if (isPathExists(filepath)) {
            return;
        }

        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPathExists(String filepath) {
        return new File(filepath).exists();
    }


}
