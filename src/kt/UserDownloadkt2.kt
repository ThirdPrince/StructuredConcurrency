package kt

import kotlinx.coroutines.*
import utils.log
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 并发下载10个User
 * 然后并发下载10个头像
 * Kotlin
 */
fun main() = runBlocking {


    val startTime = System.currentTimeMillis()
    val userIds: MutableList<Int> = ArrayList()
    for (i in 1..100) {
        userIds.add(i)
    }
    var count = userIds.size
    val map: MutableMap<Int, User> = HashMap()
    val deferredResults = userIds.map { userId ->
        async {
            val user = getUserAsync2(userId)
            log("userId-->$userId :::: user --->  $user")
            map[userId] = user
            map
        }
    }


    // 获取每个 async 任务的结果
    val results = deferredResults.map { deferred ->
        count--
        log("count  $count")
        deferred.await()

    }

    log("map -->${map.size}")
    val deferredAvatar = map.map { map ->
        async {
            getUserAvatarAsync2(map.value)
        }
    }


    var countAvatar = results.size
    val resultAvatar = deferredAvatar.map { deferred ->
        countAvatar--
        log("countAvatar  $countAvatar")
        deferred.await()

    }

    val costTime = (System.currentTimeMillis() - startTime) / 1000
    log("costTime-->$costTime")
    log("user -> $resultAvatar")
}


/**
 * 异步同步化
 */
suspend fun getUserAsync2(userId: Int): User = suspendCoroutine { continuation ->
    ClientManager.getUser(userId) {
        continuation.resume(it)
    }
}


/**
 * 异步同步化
 */
suspend fun getUserAvatarAsync2(user: User): User = suspendCoroutine { continuation ->
    ClientManager.getUserAvatar(user) {
        continuation.resume(it)
    }
}
