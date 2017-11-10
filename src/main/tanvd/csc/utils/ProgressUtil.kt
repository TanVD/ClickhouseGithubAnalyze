package tanvd.csc.utils

fun withProgress(step: Long, current: Long, body: () -> Unit) {
    if (current % step == 0L) {
        print(".")
    }
    body()
}