package kt

import kotlinx.coroutines.*
import utils.log


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
            val user = getUserAsync(userId)
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
            getUserAvatarAsync(map.value)
        }
    }


    var countAvatar = results.size
    val resultAvatar = deferredAvatar.map { deferred ->
        countAvatar--
        log("countAvatar  $countAvatar")
        deferred.await()

    }
    val costTime = (System.currentTimeMillis() - startTime)/1000
    println("costTime-->$costTime")
}


/**
 * 异步同步化
 */
suspend fun getUserAsync(userId: Int): User = withContext(ClientManager.customDispatchers){
    val sleepTime = java.util.Random().nextInt(500)
    log("getUserAsync sleepTime -->$sleepTime")
    delay(sleepTime.toLong())
    User(userId,sleepTime.toString(), "avatar", "")
}



suspend fun getUserAvatarAsync(user: User): User = withContext(ClientManager.customDispatchers) {
    val sleepTime = java.util.Random().nextInt(1000)
    log("getUserAvatarAsync sleepTime -->$sleepTime")
    delay(sleepTime.toLong())
    user.file = "$sleepTime.png"
    return@withContext user
}
