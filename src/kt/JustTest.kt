package kt

import kotlinx.coroutines.runBlocking
import utils.log

fun main() {
    runBlocking {
        ClientManager.getUser(1) {
          log("user-->$it")
        }
        log("---------")
    }
}