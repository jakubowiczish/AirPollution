package AirPollution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLReader {
    public static String getHTML() throws Exception {
        final String urlAddress = "http://api.gios.gov.pl/pjp-api/rest/station/findAll?fbclid=IwAR273c-L4bPsyTCEH9fwXf5TEvJafhq4-ntjkrJ5EyReiQxvVnFsCWx8OGI";

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
