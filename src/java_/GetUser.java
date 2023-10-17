package java_;

import kt.User;
import utils.LogKt;

/**
 * getUser callBack
 */
public class GetUser {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ClientUtils.getUser(1, new UserCallback() {
            @Override
            public void onCallback(User user) {
                LogKt.log(user.toString());
                ClientUtils.getUserAvatar(user, new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        LogKt.log(user.toString());
                        LogKt.log("costTime -->"+(System.currentTimeMillis() - startTime));
                    }
                });
            }
        });
    }
}
