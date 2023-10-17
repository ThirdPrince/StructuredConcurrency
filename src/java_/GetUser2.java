package java_;

import kt.User;
import utils.LogKt;

import java.util.concurrent.CountDownLatch;

/**
 * getUser callBack
 */
public class GetUser2 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        User user = getUser();
        user = getUserAvatar(user);
        LogKt.log(user.toString());
        LogKt.log("costTime -->"+(System.currentTimeMillis() - startTime));


    }

    /**
     * 加锁
     */
    public static User getUser() {
        CountDownLatch countDown = new CountDownLatch(1);
        User[] result = new User[1];
        ClientUtils.getUser(1, new UserCallback() {
            @Override
            public void onCallback(User user) {
                result[0] = user;
                countDown.countDown();
            }
        });
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }

    /**
     * getAvater
     *
     * @param user
     * @return
     */
    public static User getUserAvatar(User user) {
        CountDownLatch countDown = new CountDownLatch(1);
        User[] result = new User[1];
        ClientUtils.getUserAvatar(user, new UserCallback() {
            @Override
            public void onCallback(User user) {
                result[0] = user;
                countDown.countDown();
                //result = user;

            }
        });
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];

    }
}


