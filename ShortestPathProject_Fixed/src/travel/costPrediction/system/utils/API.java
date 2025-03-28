package travel.costPrediction.system.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {
    private static String apiKey = "545511b4e08c4309b6acc0669e5e3192";
    public double re[];

    public API(String city) {
        re = helper(city);
    }

    public static void main(String[] args) {
        API api = new API("Jakhal Mandi, Haryana"); // Example city
        if (api.re != null) {
            System.out.println("Latitude: " + api.re[0]);
            System.out.println("Longitude: " + api.re[1]);
        } else {
            System.out.println("❌ Could not fetch coordinates.");
        }
    }

    public double[] helper(String city) {
        String apiUrl = "https://api.geoapify.com/v1/geocode/search?text=" +
                        city.replace(" ", "%20") + "&format=json&apiKey=" + apiKey;
        double[] res = new double[2];

        try {
            // Step 1: Create a connection to the API
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Step 2: Read the API response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Step 3: Ensure we extract from the FIRST "results" array
            String jsonResponse = response.toString();
            int resultsIndex = jsonResponse.indexOf("\"results\":");
            if (resultsIndex == -1) return null; // No valid location found

            double lat = extractValue(jsonResponse, "\"lat\":", resultsIndex);
            double lon = extractValue(jsonResponse, "\"lon\":", resultsIndex);

            if (lat == 0 || lon == 0) return null; // If extraction failed

            res[0] = lat;
            res[1] = lon;

            conn.disconnect();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to extract latitude/longitude from JSON response (starting from the correct index)
    private static double extractValue(String json, String key, int startIndex) {
        int keyIndex = json.indexOf(key, startIndex);
        if (keyIndex == -1) return 0;

        keyIndex += key.length();
        int endIndex = json.indexOf(",", keyIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", keyIndex);
        }

        if (endIndex == -1) return 0;
        String valueStr = json.substring(keyIndex, endIndex).trim();
        return Double.parseDouble(valueStr);
    }
}
