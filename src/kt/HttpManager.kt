package kt

import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 模拟http 请求
 */
object HttpManager {

    private var executor: Executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun getUser(userId:Int,callback:(User)->Unit){
        executor.execute {
            val sleepTime = Random().nextInt(500)
            callback(User(userId,sleepTime.toString(), "avatar", ""))
        }
    }
}