package kt

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 并发下载10个User
 * 然后并发下载10个头像
 * Kotlin
 */
fun main() = runBlocking {


    val startTime = System.currentTimeMillis()
    val userIds = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    var count = userIds.size
    val deferredResults = userIds.map { userId ->
        async {
            getUser2(userId)
        }
    }

    // 获取每个 async 任务的结果
    val results = deferredResults.map { deferred ->
        count--
        println("count  $count")
        deferred.await()

    }
    println("get User over-->$results")
    val deferredAvatar = results.map { user ->
        async {
            getUserAvatar(user)
        }
    }
    var countAvatar = results.size
    val resultAvatar = deferredAvatar.map { deferred ->
        countAvatar--
        println("countAvatar  $countAvatar")
        deferred.await()

    }
    val costTime = (System.currentTimeMillis()-startTime)/1000
    println("costTime-->$costTime")
    println(resultAvatar.toString())
}


suspend fun getUser(userId: Int): User = withContext(Dispatchers.IO){
    val sleepTime = java.util.Random().nextInt(2000)
    delay(sleepTime.toLong())
    return@withContext User(sleepTime.toString() + "", "avatar", "")
}

/**
 * 异步同步化
 */
suspend fun getUser2(userId: Int): User = suspendCoroutine {
    continuation ->
    HttpManager.getUser(userId) {
        continuation.resume(it)
    }
}


suspend fun getUserAvatar(user: User): User= withContext(Dispatchers.IO) {
    val sleepTime = java.util.Random().nextInt(5000)
    delay(sleepTime.toLong())
    user.file = "$sleepTime.png"
    return@withContext user
}
