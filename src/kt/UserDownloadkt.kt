package kt

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 并发下载10个User
 * 然后并发下载10个头像
 * Kotlin
 */
fun main() = runBlocking {


    val dispatcherCustom = Executors.newFixedThreadPool(100).asCoroutineDispatcher()
    val startTime = System.currentTimeMillis()
    //val userIds = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    //List<Integer> userId = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    val userIds: MutableList<Int> = ArrayList()
    for (i in 1..1000) {
        userIds.add(i)
    }
    //val userList = mutableListOf<User>()
    var count = userIds.size

    val map: MutableMap<Int, User> = HashMap()
    val deferredResults = userIds.map { userId ->
        async(dispatcherCustom) {
            val user = getUser2(userId)
            map[userId] = user
            map
        }
    }


    // 获取每个 async 任务的结果
    val results = deferredResults.map { deferred ->
        count--
        println("count  $count")
        deferred.await()

    }

    println("map -->${map.size}")
    val deferredAvatar = map.map { map ->
        async(dispatcherCustom) {
            getUserAvatar(map.value)
        }
    }


    var countAvatar = results.size
    val resultAvatar = deferredAvatar.map { deferred ->
        countAvatar--
        println("countAvatar  $countAvatar")
        deferred.await()

    }
    val costTime = (System.currentTimeMillis() - startTime) / 1000
    println("costTime-->$costTime")
    // println(resultAvatar.toString())
}


/**
 * 异步同步化
 */
suspend fun getUser2(userId: Int): User = suspendCoroutine { continuation ->
    HttpManager.getUser(userId) {
        continuation.resume(it)
    }
}


suspend fun getUserAvatar(user: User): User = withContext(Dispatchers.IO) {
    val sleepTime = java.util.Random().nextInt(1000)
    delay(sleepTime.toLong())
    user.file = "$sleepTime.png"
    return@withContext user
}
