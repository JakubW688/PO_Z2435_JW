import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSearch {
    public static List<String> searchInFile(String searchTerm, String filePath) {
        List<String> matchingLines = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            matchingLines.add("Plik " + filePath + " nie istnieje.");
            return matchingLines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingLines.add(line);
                }
            }
        } catch (IOException e) {
            matchingLines.add("Błąd odczytu pliku: " + e.getMessage());
        }

        return matchingLines;
    }
}

