package java_;

import kt.User;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟网络请求
 * Java 版本
 */
public class HttpUtils {
    static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    /**
     * getUser
     * @param userId
     * @param userCallback
     */
    public static  void getUser(int userId,UserCallback userCallback){
        executorService.execute(() -> {
           long  sleepTime  = new Random().nextInt(500);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userCallback.onCallback(new User(userId,sleepTime+"", "avatar", ""));
        });

    }

    /**
     * getAvatar
     * @param user
     * @param userCallback
     * @throws InterruptedException
     */
    public static void getUserAvatar(User user,UserCallback userCallback) throws InterruptedException {

        executorService.execute(()->{
            int sleepTime = new Random().nextInt(1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user.setFile(sleepTime + ".png");
            userCallback.onCallback(user);
        });
    }


}

interface UserCallback{
    void onCallback(User user);
}
