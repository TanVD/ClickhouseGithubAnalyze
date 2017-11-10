package tanvd.csc

import tanvd.aorm.Row
import tanvd.aorm.insertWithColumns
import tanvd.aorm.values
import tanvd.csc.clickhouse.PushEventsTable
import tanvd.csc.utils.JsonUtils
import java.io.File

object EventsConverter {
    fun uploadFile(file: File, repoMap: Map<String, String>) {
        val rows = ArrayList<Row>()
        file.forEachLine { line ->
            val event = JsonUtils.readValue(line, PushEvent::class)
            rows.addAll(event.toRows(repoMap))
            if (rows.size > 50000) {
                println("STARTING FLUSH")
                flushRows(rows)
                println("FLUSHED")
            }
        }
        flushRows(rows)
    }

    private fun flushRows(rows: ArrayList<Row>) {
        if (rows.isNotEmpty()) {
            PushEventsTable insertWithColumns rows.first().columns values rows
            rows.clear()
        }
    }
}