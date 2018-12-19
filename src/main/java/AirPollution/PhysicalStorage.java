package AirPollution;

import java.io.*;

public class PhysicalStorage {
    Storage dataStorage = new Storage();



    private static String receiveFile(File inputFile) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            if(!inputFile.isFile()){
                throw new FileNotFoundException("Nie znaleziono pliku o podanej nazwie, sprobuj ponownie");

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
