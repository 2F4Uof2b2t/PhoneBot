package net.twoh2e.CallUtils;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import net.twoh2e.Commands.CallCommand;
import net.twoh2e.Main;

import java.awt.*;

public class CallHandler {

    public CallServer line1; //the call starter
    public CallServer line2; //the call reciever
    public boolean isConnected; // still ringing?

    public CallHandler(CallServer server1, String server2num) {
        try {
            if (!Main.api.getServers().contains(Main.api.getServerById(server2num))) {
                // cant complete call
                server1.channel.sendMessage("-/- **Call routing failure, phone call can't be placed** -/-");
                this.close();
                return;
            }
        }catch (NullPointerException e) {
            server1.channel.sendMessage("-/- **Call routing failure, phone call can't be placed** -/-");
            this.close();
            return;
        }
        line1 = server1;
        line2 = new CallServer(Main.api.getServerById(server2num));
        isConnected = false;
        CallCommand.handlers.add(this);
        line1.channel.sendMessage("-...- Placing call. -...-");
        this.sendRing();
        new Thread(() -> {
            try {
                Thread.sleep(30000);
                if (!this.isConnected && CallCommand.handlers.contains(this)) {
                    this.timeOut();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handleDialogue(Message message) {
        if (message.getChannelReceiver().getServer() == line1.server) {
            line2.channel.sendMessage(">> *" + message.getContent() + "* >>");
            return;
        }
        if (message.getChannelReceiver().getServer() == line2.server) {
            line1.channel.sendMessage(">> *" + message.getContent() + "* >>");
            return;
        }
        else {
            // :/ why and how?
            line1.channel.sendMessage("-/- ***A major error occured*** -/-");
            line2.channel.sendMessage("-/- ***A major error occured*** -/-");
            this.close();
        }
    }

    public void sendRing() {
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(Color.GREEN);
        b.setTitle("Incoming Call");
        b.setDescription("Call from \"" + line1.server.getName());
        b.setThumbnail(line1.server.getIconUrl().toString());
        line2.channel.sendMessage("", b);
        line2.submitRing(line1);
        isConnected = false;
        EmbedBuilder cb = new EmbedBuilder();
        cb.setColor(Color.YELLOW);
        cb.setTitle("Calling...");
        cb.setDescription("Calling \"" + line2.server.getName() + "\"");
        cb.setThumbnail(line2.server.getIconUrl().toString());
        line1.channel.sendMessage("", cb);
    }

    public void acceptRing() {
        isConnected = true;
        line1.channel.sendMessage("--- Call accepted. ---");
    }

    public void rejectRing() {
        line1.channel.sendMessage("->  <- Call rejected. ->  <-");
        this.close();
    }

    public void endCall() {
        isConnected = false;
        line1.channel.sendMessage("->  <- Call ended. ->  <-");
        line2.channel.sendMessage("->  <- Call ended. ->  <-");
        this.close();
    }
    public void timeOut() {
        isConnected = false;
        line1.channel.sendMessage("-%- Call timed out, no answer. -%-");
        line2.channel.sendMessage("-%- Call timed out, no answer. -%-");
        this.close();
    }

    public void close() {
        CallCommand.handlers.remove(this);
    }

    public boolean isServerInCall(Server server) {
        if (!this.isConnected) {
            return false;
        }
        if (server == line1.server) {
            return true;
        }
        if (server == line2.server) {
            return true;
        }
        return false;
    }
}
