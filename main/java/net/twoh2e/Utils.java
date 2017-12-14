package net.twoh2e;

import de.btobastian.javacord.entities.User;

/**
 * Created by Sasha Stevens on 6/2/2017.
 */
public class Utils {

    static boolean isOp(User user) {
        System.out.println(user.getMentionTag());
        return MessageListener.ops.contains(user)/* || user.getMentionTag().equals("<@225403025043226625>")*/;
    }
}
