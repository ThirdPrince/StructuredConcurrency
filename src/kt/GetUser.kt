package kt

import kotlinx.coroutines.runBlocking
import utils.log
import kotlin.system.measureTimeMillis

/**
 * Coroutines
 */
fun main() = runBlocking {
    val costTime = measureTimeMillis {
        val user = getUserAsync(1);
        val userAvatar = getUserAvatarAsync2(user)
        log(userAvatar.toString())
    }
    log("cost -->$costTime")


}