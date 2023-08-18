package java_;

import kt.User;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpUtils {
    static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static  void getUser(int userId,UserCallback userCallback){
        executorService.execute(() -> {
           long  sleepTime  = new Random().nextInt(500);
            userCallback.onCallback(new User(userId,sleepTime+"", "avatar", ""));
        });

    }

}

interface UserCallback{
    void onCallback(User user);
}
