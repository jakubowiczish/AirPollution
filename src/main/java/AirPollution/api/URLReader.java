package AirPollution.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class used to fetch URL address
 */
public class URLReader {
    /**
     * Fetches content from URL Address to String
     *
     * @param urlAddress desired URL Address
     * @return fetched content from URL Address to String
     * @throws IOException exception
     */
    public static String getJSON(String urlAddress) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlAddress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}