package com.example.itubeapp.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YouTubeApiHelper {

    private static final String API_KEY = "YOUR_API_KEY";

    private static boolean isValidYouTubeUrl(String url) {
        // YouTube URL regex pattern
        String pattern = "^(http(s)?:\\/\\/)?(www\\.)?((youtube\\.com\\/watch\\?v=)|(youtu.be\\/))[a-zA-Z0-9_-]{11}$";
        return url.matches(pattern);
    }

    public static String getYouTubeVideoId(String url) {
        if (!isValidYouTubeUrl(url)) {
            return null;
        }

        return url.split("v=")[1];
    }

    // https://developers.google.com/youtube/iframe_api_reference#Mobile_considerations
    public static String getYouTubePlayerHtml(String videoId) {
        return
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "  <body>\n" +
                        "    <div id=\"player\"></div>\n" +
                        "\n" +
                        "    <script>\n" +
                        "      var tag = document.createElement('script');\n" +
                        "\n" +
                        "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                        "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                        "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                        "\n" +
                        "      var player;\n" +
                        "      function onYouTubeIframeAPIReady() {\n" +
                        "        player = new YT.Player('player', {\n" +
                        "          height: '820',\n" +
                        "          width: '400',\n" +
                        "          videoId: '" + videoId + "',\n" +
                        "          events: {\n" +
                        "            'onReady': onPlayerReady,\n" +
                        "            'onStateChange': onPlayerStateChange\n" +
                        "          }\n" +
                        "        });\n" +
                        "      }\n" +
                        "\n" +
                        "      function onPlayerReady(event) {\n" +
                        "      }\n" +
                        "\n" +
                        "      var done = false;\n" +
                        "      function onPlayerStateChange(event) {\n" +
                        "      }\n" +
                        "\n" +
                        "      function stopVideo() {\n" +
                        "        player.stopVideo();\n" +
                        "      }\n" +
                        "    </script>\n" +
                        "  </body>\n" +
                        "</html>";
    }

    public static boolean isYouTubeVideoPlayable(String urlString) throws IOException {
        if (!isValidYouTubeUrl(urlString)) {
            return false;
        }

        String videoId = getYouTubeVideoId(urlString);

        // Construct the API request URL
        String requestUrl = "https://www.googleapis.com/youtube/v3/videos?key=" + API_KEY + "&part=status&id=" + videoId;


        // Make the API request
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the API response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Process the API response
        return isVideoPlayable(response.toString());
    }

    private static boolean isVideoPlayable(String response) {
        // Example response:
        //{
        //  "kind": "youtube#videoListResponse",
        //  "etag": "SWWgeqkfTbZ-E5eZqxCX-QiBgnA",
        //  "items": [
        //    {
        //      "kind": "youtube#video",
        //      "etag": "xlQj8dBtHtkSzXgcgFNqLV3hfbA",
        //      "id": "RbAi_LkwCOM",
        //      "status": {
        //        "uploadStatus": "processed",
        //        "privacyStatus": "public",
        //        "license": "youtube",
        //        "embeddable": true,
        //        "publicStatsViewable": true,
        //        "madeForKids": false
        //      }
        //    }
        //  ],
        //  "pageInfo": {
        //    "totalResults": 1,
        //    "resultsPerPage": 1
        //  }
        //}

        JsonElement jsonElement = JsonParser.parseString(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return jsonObject.getAsJsonArray("items").size() != 0;
    }
}