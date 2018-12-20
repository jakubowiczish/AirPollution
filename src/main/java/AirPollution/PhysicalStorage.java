package AirPollution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class PhysicalStorage {
    private static final String filePath = "/home/plotnikowski/IdeaProjects/AirPollution/src/PhysicalStorage.json";

    Storage loadStorageFromFile() {
        Storage storage;
        Gson gson = new Gson();
        try {
            storage = gson.fromJson(receiveFile(new File(filePath)), Storage.class);
        } catch (Exception e) {
            System.out.println("Error while loading storage from file: " + e);
            return null;
        }
        return storage;
    }

    void saveStorageToFile(Storage storage) {
        Writer writer = null;
        try {
            writer = new FileWriter(filePath);
            Gson gson = new GsonBuilder().create();
            gson.toJson(storage, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private static String receiveFile(File inputFile) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            if (!inputFile.isFile()) {
                throw new FileNotFoundException("No such file found, try again");

            }
            bufferedReader = new BufferedReader(new FileReader(inputFile));

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

            return stringBuilder.toString();

        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
