package kt

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking{
    val userPortraitImageMap: MutableMap<Long, String> = mutableMapOf()

    userPortraitImageMap[1]  = "123"
    userPortraitImageMap[2] =  "456"
    userPortraitImageMap[3] = "789"
    userPortraitImageMap[4] = "012"


    if (userPortraitImageMap.isNotEmpty()) {
        coroutineScope {
            val deferredResults = userPortraitImageMap.map { (userId, fileId) ->
                async {
                    log("userId -->$userId 开始")
                    val localPath = downloadPortraitPhoto(fileId)
                    userId to localPath
                }
            }
            println(deferredResults::class.simpleName)
            val resultMap = deferredResults.map { deferred ->
                val (userId, localPath) = deferred.await()
                log("userId -->$userId 结束")
                userId to localPath
            }
            log("全部 结束")
            resultMap.forEach {
                println("${it.first}"+":${it.second}")
            }

            // Now you have a map where keys are User IDs and values are local file paths
            // You can use resultMap to associate User IDs with their corresponding local file paths
        }
    }

    println("--------")

}

suspend fun  downloadPortraitPhoto(fileId :String):String{
    val sleepTime = java.util.Random().nextInt(10000)
    log("downloadPortraitPhoto-->$sleepTime")
    delay(sleepTime.toLong())
    return "${fileId}.png"

}