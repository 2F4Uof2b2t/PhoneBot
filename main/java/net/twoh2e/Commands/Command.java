package net.twoh2e.Commands;

import com.sun.istack.internal.NotNull;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import net.twoh2e.CallUtils.CallHandler;
import net.twoh2e.CallUtils.CallServer;

public class Command {

    private String commandname;
    private Channel channel;
    private String wholeMessage;
    private boolean admin;
    private String[] args;
    private User sender;
    private CallHandler handler = null; // for accept/reject/end

    public static String delimetre = "^";

    public Command(String commandname, boolean admin) {
        this.commandname = commandname;
        this.admin = admin;
    }


    public String getCommandName() {
        return delimetre + this.commandname;
    }

    public String[] getArgs() {
        return this.args;
    }

    public void setArgs(String... args) {
        this.args = args;
    }

    public void onCommand() {}
    public void onCommand(CallHandler handler) {}

    public Channel getChannel() {
        return channel;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public String getWholeMessage() {
        return this.wholeMessage;
    }
    public void setWholeMessage(String message) {
        this.wholeMessage = message;
    }
    public boolean isAdministrative() {
        return this.admin;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public User getSender() {
        return this.sender;
    }
}