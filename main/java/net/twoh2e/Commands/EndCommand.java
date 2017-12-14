package net.twoh2e.Commands;

import net.twoh2e.CallUtils.CallHandler;

public class EndCommand extends Command {

    public EndCommand() {
        super("done", false);
    }

    public void onCommand(CallHandler handler){
        if (!handler.isConnected) {
            getChannel().sendMessage("-?- A call is not connected -?-");
            return;
        }
        handler.endCall();
    }
}
