package net.twoh2e.Commands;

import net.twoh2e.CallUtils.CallHandler;

public class RejectCommand extends Command {

    //public static ArrayList<CallHandler> handlers = new ArrayList<>();

    public RejectCommand() {
        super("reject", false);
    }

    public void onCommand(CallHandler handler){
        if (handler.isConnected) {
            getChannel().sendMessage("<---> A call is already connected <--->");
            return;
        }
        handler.rejectRing();
    }
}
