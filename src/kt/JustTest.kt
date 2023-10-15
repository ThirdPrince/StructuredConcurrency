package kt

import kotlinx.coroutines.runBlocking
import utils.log

fun main() {
    runBlocking {
        HttpManager.getUser(1) {
          log("user-->$it")
        }
        log("---------")
    }
}