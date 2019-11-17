package AirPollution.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.util.stream.Collectors.joining;

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
    static String getJSON(String urlAddress) throws IOException {
        String result;
        URL url = new URL(urlAddress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        result = rd.lines()
                .collect(joining());
        rd.close();

        return result;
    }
}