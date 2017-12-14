package net.twoh2e.Commands;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NamecheckCommand extends Command {

    private static Channel theChannel = null;

    public NamecheckCommand() {
        super("namecheck", false);
    }
    public void onCommand()
    {
        System.out.println("nig");
        theChannel = getChannel();
        getChannel().type();
        if (getArgs() == null)
        {
            EmbedBuilder e = new EmbedBuilder();
            e.setColor(Color.RED);
            e.setTitle("Missing Arguments");
            e.setDescription("Correct usage > -namemc <playername>");
            getChannel().sendMessage("", e);
            return;
        }
        try
        {
            String uuid = getUUID(getArgs()[1]);
            ArrayList<String> str = getUsernameHistory(uuid);
            StringBuilder builder = new StringBuilder();
            for (String s : str)
            {
                builder.append(s.replace("_", "\\_") + "\n");
                System.out.println(s);
            }
            EmbedBuilder b = new EmbedBuilder();
            b.setTitle("Username history of " + getArgs()[1]);
            b.setDescription(builder.toString());
            b.setColor(Color.cyan);
            b.setFooter("(Most recent names show up at the bottom.)");
            getChannel().sendMessage("Status: ***TAKEN***", b);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            EmbedBuilder b = new EmbedBuilder();
            b.setColor(Color.RED);
            b.setTitle("Invalid Username!");
            b.setDescription("Hey, that isn't a valid username!");
            getChannel().sendMessage("Status: ***AVAILABLE*** or ***AVAILABLE LATER*** (?)", b);
        }
    }
    public static String getUUID(String username)
            throws IOException
    {
        String apiurl = "https://api.mojang.com/users/profiles/minecraft/" + username.replace(" ", "");
        URL url = new URL(apiurl);
        System.out.println(apiurl);
        URLConnection cnt = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
        StringBuilder b = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            b.append(line + "\n");
        }
        reader.close();

        JSONObject obj = new JSONObject(b.toString());
        return obj.getString("id");
    }

    public static ArrayList<String> getUsernameHistory(String UUID)
            throws IOException
    {
        ArrayList<String> str = new ArrayList();
        String apiurl = "https://api.mojang.com/user/profiles/" + UUID + "/names";
        URL url = new URL(apiurl);
        System.out.println(apiurl);
        URLConnection cnt = url.openConnection();
        BufferedReader r = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
        StringBuilder b = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            b.append(line + "\n");
        }
        r.close();
        JSONArray arr = new JSONArray(b.toString());
        for (int i = 0; i < arr.length(); i++)
        {
            JSONObject obj = arr.getJSONObject(i);
            String nameFormatted = obj.getString("name");
            System.out.println(nameFormatted);
            try
            {
                String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(obj.getLong("changedToAt")));
                nameFormatted = nameFormatted + " @ " + dateString;
                str.add(nameFormatted);
            }
            catch (Exception e)
            {
                str.add(nameFormatted + " @ Before existence");
            }
        }
        return str;
    }
}
