package net.twoh2e;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import net.twoh2e.Commands.Command;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Sean Stevens on 6/1/2017.
 */
public class MessageListener implements MessageCreateListener{

    public static ArrayList<User> ops = new ArrayList<>();
    public static ArrayList<User> muted = new ArrayList<User>();

    static String[] str = {"<@192844311367254017> no", "<@192844311367254017> nope", "<@192844311367254017> naw", "<@192844311367254017> hell naw", "<@192844311367254017> nosir", "<@192844311367254017> fuck off", "<@192844311367254017> lmao", "<@192844311367254017> fucking nerd", "<@192844311367254017> fuck off with your zozzle shit", "<@192844311367254017> wonder why everyone hates you", "<@192844311367254017> stop harassing me", "<@192844311367254017> gayfag", "<@192844311367254017> pls fix this"};

    public void onMessageCreate(DiscordAPI discordAPI, Message message) {
        if (muted.contains(message.getAuthor())) {
            EmbedBuilder b = new EmbedBuilder();
            b.setColor(Color.RED);
            b.setTitle("You are muted!");
            b.setDescription(message.getContent());
            b.setFooter("You cannot send this message - you are muted.");
            message.getAuthor().sendMessage("", b);
            message.delete();
            return;
        }
        for (Command cmd : Main.getCommands()) {
            if (message.getContent().startsWith(cmd.getCommandName()) && !Objects.equals(message.getAuthor().getId(), discordAPI.getYourself().getId())) {
                if (message.getContent().contains(" ")) {
                    cmd.setArgs(message.getContent().split(" "));
                }
                cmd.setChannel(message.getChannelReceiver());
                cmd.setSender(message.getAuthor());
                cmd.setWholeMessage(message.getContent());

                if (cmd.isAdministrative() && !hasAdminRole(message.getAuthor(), message.getChannelReceiver().getServer())) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("No permission");
                    builder.setDescription("You do not have permission to execute this command");
                    builder.setColor(Color.RED);
                    message.reply("", builder);
                    return;
                }
                cmd.onCommand();
            }
        }
    }
    private static boolean hasAdminRole(User user, Server server) {
        for (Role role : user.getRoles(server)) {
            if (role.getName().equalsIgnoreCase("2f4uadmin")) {
                return true;
            }
        }
        return false;
    }
}
