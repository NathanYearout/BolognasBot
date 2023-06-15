package me.sudden.discordbot.handlers;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class apiHandler {
    public static JSONObject executePost(String targetURL, String urlParameters) {
        try {
            URL url = new URL(targetURL + urlParameters);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONObject jObject = new JSONObject(inline); // json

                //String projectname = data.getString("name"); // get the name from data.

                //JSONObject data_obj = (JSONObject) parse.parse(inline);

                return jObject;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
