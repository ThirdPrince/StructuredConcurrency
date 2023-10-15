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

import static utils.LogKt.log;

/**
 * 并发下载10个User
 * 然后并发下载10个头像
 * Java
 */
public class UserDownload {

    public static void main(String[] args) throws InterruptedException {

        long  startTime = System.currentTimeMillis();
        List<Integer> userId = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            userId.add(i);
        }
        Map<Integer,User> map = new ConcurrentHashMap<>();
        log("开始下载user");
        AtomicInteger atomicInteger  = new AtomicInteger(userId.size());
        CountDownLatch countDownLatch = new CountDownLatch(userId.size());
        for (Integer id : userId) {
            HttpUtils.getUser(id, user -> {
                log("atomicInteger-->"+  atomicInteger.decrementAndGet());
                map.put(id,user);
                countDownLatch.countDown();
            });

        }
        countDownLatch.await();
        log("atomicInteger-->"+atomicInteger.get());
        log("开始下载头像");

        AtomicInteger atomicIntegerAvatar  = new AtomicInteger(userId.size());
        CountDownLatch countDownLatchDownload= new CountDownLatch(userId.size());
        log("map size-->"+map.size());
        for (User user : map.values()){
            HttpUtils.getUserAvatar(user, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    log("atomicIntegerAvatar-->"+  atomicIntegerAvatar.decrementAndGet());
                    map.put(user.getUserId(),user);
                    countDownLatchDownload.countDown();
                }
            });

        }
        countDownLatchDownload.await();
        long costTime = (System.currentTimeMillis() -startTime)/1000;
        //log(map.toString());
        log("costTime -->"+costTime);
    }

}

