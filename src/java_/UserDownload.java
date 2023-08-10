package java_;

import kotlin.reflect.KVariance;
import kt.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发下载10个User
 * 然后并发下载10个头像
 * Java 
 */
public class UserDownload {

    public static void main(String[] args) throws InterruptedException {

        long  startTime = System.currentTimeMillis();
        Executor executor = Executors.newFixedThreadPool(10);
        List<Integer> userId = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        List<User> users = new ArrayList<>();

        System.out.println("开始下载user");
        AtomicInteger atomicInteger  = new AtomicInteger(userId.size());
        CountDownLatch countDownLatch = new CountDownLatch(userId.size());
        for (Integer id : userId) {
            executor.execute(() -> {
                try {
                    users.add(getUser(id));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("atomicInteger-->"+  atomicInteger.decrementAndGet());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("atomicInteger-->"+atomicInteger.get());
        System.out.println("开始下载头像");

        AtomicInteger atomicIntegerAvatar  = new AtomicInteger(userId.size());
        CountDownLatch countDownLatchDownload= new CountDownLatch(userId.size());

        for (User user : users){
            executor.execute(() -> {
                try {
                   getUserAvatar(user);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("atomicIntegerAvatar-->"+  atomicIntegerAvatar.decrementAndGet());
                countDownLatchDownload.countDown();
            });
        }
        countDownLatchDownload.await();
        long costTime = (System.currentTimeMillis() -startTime)/1000;
        System.out.println(users.toString());
        System.out.println("costTime -->"+costTime);


    }

    public static User getUser(int userId) throws InterruptedException {
        int sleepTime = new Random().nextInt(2000);
        Thread.sleep(sleepTime);
        return new User(sleepTime + "", "avatar","");
    }

    public static User getUserAvatar(User user) throws InterruptedException {
        int sleepTime = new Random().nextInt(5000);
        Thread.sleep(sleepTime);
        user.setFile(sleepTime + ".png");
        return user;
    }
}

