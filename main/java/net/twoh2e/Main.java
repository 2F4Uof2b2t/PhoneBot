package net.twoh2e;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.message.MessageDeleteListener;
import de.btobastian.javacord.listener.message.MessageEditListener;
import net.twoh2e.CallUtils.CallHandler;
import net.twoh2e.Commands.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Sean Stevens on 6/1/2017.
 */
public class Main {


    public static DiscordAPI api = Javacord.getApi("INSERT YOUR TOKEN HERE", true);
    //public static DiscordAPI api = Javacord.getApi("", "");
    static MessageListener listener = new MessageListener();
    private static ArrayList<Command> commands;
    public static boolean kekMode = true;

    //public static HashMap<String, String> messageMap = new HashMap<>(); // Message ID | Content

    public static void main(String[] args) {
        api.connect(new FutureCallback<DiscordAPI>() {
            public void onSuccess(final DiscordAPI api) {
                commands = new ArrayList<>();
                //
                loadCommands();
                //registerCMD(new NameCommand());
                //
                //api.registerListener(listener);
                api.registerListener((MessageCreateListener) (discordAPI, message) -> {
                    System.out.println("\"" + message.getContent() + "\"");
                    if (message.getContent().toLowerCase().startsWith(Command.delimetre + "end")) {
                        for (CallHandler handler : CallCommand.handlers) {
                            if(message.getChannelReceiver().getServer() == handler.line2.server || message.getChannelReceiver().getServer() == handler.line1.server) {
                                if (!handler.isConnected) {
                                    message.getChannelReceiver().sendMessage("-?- A call is not connected -?-");
                                    return;
                                }
                                handler.endCall();
                                return;
                            }
                        }
                    }
                    for(Command command : commands) {
                        if (message.getContent().toLowerCase().startsWith(command.getCommandName())) {
                            command.setArgs(message.getContent().split(" "));
                            command.setChannel(message.getChannelReceiver());
                            command.setSender(message.getAuthor());
                            command.setWholeMessage(message.getContent());
                            command.onCommand();
                            for (CallHandler handler : CallCommand.handlers) {
                                if(command.getChannel().getServer() == handler.line2.server) {
                                    command.onCommand(handler);
                                    return;
                                }
                            }
                        }
                        for (CallHandler handler : CallCommand.handlers) {
                            if (handler.isServerInCall(message.getChannelReceiver().getServer())) {
                                if (message.getAuthor() != api.getYourself()) {
                                    handler.handleDialogue(message);
                                    return;
                                }
                            }
                        }
                    }
                    Channel chan = message.getChannelReceiver();
                    //messageMap.put(message.getId(), message.getContent());
                });
                api.registerListener(new MessageDeleteListener() {
                    @Override
                    public void onMessageDelete(DiscordAPI discordAPI, Message message) {
                    }
                });
                api.registerListener(new MessageEditListener() {
                    @Override
                    public void onMessageEdit(DiscordAPI discordAPI, Message message, String s) {

                    }
                });
                Server server;
                for (Server s : api.getServers()) {
                    server = s;
                }
            }
//
            //@Override
            public void onFailure(Throwable t) {
                // login failed
                t.printStackTrace();
            }
        });
    }
    private static void registerCMD(Command command) {
        commands.add(command);
        System.out.println(command.getCommandName() + " successfully registered!");
    }
    public static ArrayList<Command> getCommands() {
        return commands;
    }
    public static void loadCommands() {
        registerCMD(new NamecheckCommand());
        registerCMD(new AnswerCommand());
        registerCMD(new RejectCommand());
        //registerCMD(new EndCommand());
        registerCMD(new CallCommand());
    }

    public static boolean isAllowedToEmbed(Channel channel, User yourself) {
        for (Role role : yourself.getRoles(channel.getServer())) {
            if (role.getPermissions().getState(PermissionType.EMBED_LINKS) == (PermissionState.ALLOWED) || role.getPermissions().getState(PermissionType.EMBED_LINKS) == (PermissionState.NONE)) {
                return true;
            }
        }
        if (channel.getOverwrittenPermissions(yourself).getState(PermissionType.EMBED_LINKS) == (PermissionState.ALLOWED) || channel.getOverwrittenPermissions(yourself).getState(PermissionType.EMBED_LINKS) == (PermissionState.NONE)) {
            return true;
        }
        return false;
    }
}
