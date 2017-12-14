package net.twoh2e.Commands;

import net.twoh2e.CallUtils.CallHandler;
import net.twoh2e.CallUtils.CallServer;

import java.util.ArrayList;

public class AnswerCommand extends Command {

    //public static ArrayList<CallHandler> handlers = new ArrayList<>();

    public AnswerCommand() {
        super("accept", false);
    }

    public void onCommand(CallHandler handler){
        if (handler.isConnected) {
            getChannel().sendMessage("<---> A call is already connected <--->");
            return;
        }
        handler.acceptRing();
    }
}
