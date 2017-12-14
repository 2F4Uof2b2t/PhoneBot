package net.twoh2e.CallUtils;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import javafx.util.Pair;

public class CallServer {

    private boolean isInCall;
    private boolean isRinging;
    public Server server;
    public Channel channel;
    private String phoneNumber;
    private CallServer otrServ = null;

    public CallServer(Server server) {
        isInCall = false;
        isRinging = false;
        this.server = server;
        phoneNumber = server.getId();
        for (Channel channel : server.getChannels()) {
            if (channel.getName().toLowerCase().contains("phonebot")) {
                this.channel = channel;
            }
        }
    }
    public void submitRing(CallServer foreignServer) {
        isRinging = true;
        otrServ = foreignServer;
    }
}
