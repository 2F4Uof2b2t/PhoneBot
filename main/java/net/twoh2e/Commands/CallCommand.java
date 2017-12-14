package net.twoh2e.Commands;

import de.btobastian.javacord.entities.Server;
import net.twoh2e.CallUtils.CallHandler;
import net.twoh2e.CallUtils.CallServer;

import java.util.ArrayList;

public class CallCommand extends Command {

    public static ArrayList<CallHandler> handlers = new ArrayList<>();

    public CallCommand() {
        super("call", false);
    }

    public void onCommand(){
        if (this.getArgs() == null || this.getArgs()[1].equalsIgnoreCase("")) {
            this.getChannel().sendMessage("-!- You forgot to input a server phone number. -!-\n`^call <server id>`");
            return;
        }
        if (isSomeServerAlreadyCalling(getChannel().getServer())) {
            this.getChannel().sendMessage("-!- This server is already in a call -!-");
            return;
        }
        CallServer line1 = new CallServer(this.getChannel().getServer());
        //CallServer line2 = new CallServer(Main.api.getServerById(this.getArgs()[0]));
        if (this.getArgs()[1].equalsIgnoreCase(this.getChannel().getServer().getId())) {
            this.getChannel().sendMessage("-&- You cannot call yourself! -&-");
            return;
        }
        new CallHandler(line1, this.getArgs()[1]);
    }
    public static boolean isSomeServerAlreadyCalling(Server serb) {
        for (CallHandler handle : handlers) {
            if (handle.line1.server == serb || handle.line2.server == serb) {
                return true;
            }
        }
        return false;
    }
}
