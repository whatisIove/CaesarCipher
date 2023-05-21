package crypto.system;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader reader = new FileReader(file)) {
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }
        }

        return content.toString();
    }

    public static void writeFile(File file, String content) throws IOException {

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
