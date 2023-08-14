package kt

fun main() {

   val res =   123 to "jjj"

    println(res::class.simpleName)

    val fruitMap = mapOf(
        "apple" to 5,
    )
    println(fruitMap::class.simpleName)
    println(fruitMap)

}