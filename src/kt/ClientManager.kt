package kt

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 模拟客户端请求 请求
 */
object ClientManager {

    var executor: Executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)
    val customDispatchers = executor.asCoroutineDispatcher()

    /**
     * getUser
     */
    fun getUser(userId: Int, callback: (User) -> Unit) {
        executor.execute {
            val sleepTime = Random().nextInt(500)
            Thread.sleep(sleepTime.toLong())
            callback(User(userId, sleepTime.toString(), "avatar", ""))
        }
    }

    /**
     * getAvatar
     */
    fun getUserAvatar(user: User, callback: (User) -> Unit) {
        executor.execute {
            val sleepTime = Random().nextInt(1000)
            try {
                Thread.sleep(sleepTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            user.file = "$sleepTime.png"
            callback(user)
        }
    }


}