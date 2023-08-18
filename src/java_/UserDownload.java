package java_;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KVariance;
import kt.HttpManager;
import kt.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Integer> userId = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            userId.add(i);
        }
        Map<Integer,User> map = new ConcurrentHashMap<>();
        System.out.println("开始下载user");
        AtomicInteger atomicInteger  = new AtomicInteger(userId.size());
        CountDownLatch countDownLatch = new CountDownLatch(userId.size());
        for (Integer id : userId) {
            HttpUtils.getUser(id, user -> {
                map.put(id,user);
                countDownLatch.countDown();
            });

        }
        countDownLatch.await();
        System.out.println("atomicInteger-->"+atomicInteger.get());
        System.out.println("开始下载头像");

        AtomicInteger atomicIntegerAvatar  = new AtomicInteger(userId.size());
        CountDownLatch countDownLatchDownload= new CountDownLatch(userId.size());
        System.out.println("map size-->"+map.size());
        for (User user : map.values()){
            //System.out.println("---"+user);
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
        System.out.println(map.toString());
        System.out.println("costTime -->"+costTime);


    }


    public static User getUserAvatar(User user) throws InterruptedException {
        int sleepTime = new Random().nextInt(1000);
        Thread.sleep(sleepTime);
        user.setFile(sleepTime + ".png");
        return user;
    }
}

