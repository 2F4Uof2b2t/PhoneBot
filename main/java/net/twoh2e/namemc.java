package net.twoh2e;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Created by Sean Stevens on 6/2/2017.
 */
public class namemc {

    private static Gson gson;

    public static String[] getNames(String name) {
        String[] namess = {};
        try {
            JsonElement json = getJsonFromName(name);
            MojangName names[] = getGson().fromJson(json, MojangName[].class);

            namess[0] = name + "'s previous names";
            int count = 0;
            for (MojangName n : names) {
                count++;
                if (isAvailable(n.name)) {
                    namess[count] = n.name + " - (TAKEN)";
                }
                else {
                    namess[count] = n.name + " - (AVAILABLE)";
                }
                //mc.player.addChatMessage(new TextComponentString(n.getName()));
            }return namess;
        } catch (IOException e) {
            //Nova.errorMessage("IOException thrown.");
            e.printStackTrace();
        }
        return null;
    }

    private static Gson getGson(){
        return gson;
    }

    static JsonParser parser = new JsonParser();

    // @StringEncryption
    public static JsonElement getJsonFromName(String name) throws IOException {
        long unixTime = System.currentTimeMillis();
        URL url;

        url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + unixTime);

        String json = getSourceFromURL(url);
        String uuid = getGson().fromJson(parser.parse(json).getAsJsonObject().get("id"), String.class);

        url = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");

        return parser.parse(getSourceFromURL(url));
    }

    public static UUID getUUID(String name) throws IOException {
        long ut = System.currentTimeMillis();
        URL url;
        url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + ut);
        String json = getSourceFromURL(url);
        String uuidunformatted =  getGson().fromJson(parser.parse(json).getAsJsonObject().get("id"), String.class);
        String f1 = uuidunformatted.substring(0, 7) + "-";
        String f2 = uuidunformatted.substring(8, 11) + "-";
        String f3 = uuidunformatted.substring(12, 15) + "-";
        String f4 = uuidunformatted.substring(16, 20) + "-";
        String f5 = uuidunformatted.substring(21, uuidunformatted.length());
        String uuidformatted = f1 + f2 + f3 + f4 + f5;
        //Client.logInfo(uuidformatted);
        return UUID.fromString(uuidformatted);
        //dab88d24-80e0-40d8-b5df-5793305b74d4
        //12345678 9012 3456 7892012345667
    }   //          (10)

    // @StringEncryption
    public static String getSourceFromURL(URL url) throws IOException {

        InputStreamReader isr = null;
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();
        isr = new InputStreamReader(is);


        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder sb = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();

        isr.close();

        return result;
    }
    // @StringEncryption
    public static boolean isAvailable(String name) {
        long unixTime = System.currentTimeMillis();
        try {
            URL e = new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + unixTime);
            URLConnection connection = e.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains(name)) {
                    return true;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private class MojangName {
        private String name;
        public String getName() {
            return this.name;
        }
    }
}
