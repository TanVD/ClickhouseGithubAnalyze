package tanvd.csc

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import tanvd.aorm.Column
import tanvd.aorm.DbType
import tanvd.aorm.Row
import tanvd.csc.clickhouse.PushEventsTable
import java.util.*

data class PushEvent(val r: String, val c: List<Commit>) {
    val patternNumbers = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z").withOffsetParsed()
    val patternNumbersShort = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    val patternWords = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy Z").withOffsetParsed()
    val patternWordsShort = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy")

    fun toRows(repoMap: Map<String, String>): List<Row> {
        val rows = ArrayList<Row>()
        c.forEach { commit ->
            try {
                val (time, localTime) = when {
                    patternNumbers.canParse(commit.t) -> {
                        DateTime.parse(commit.t, patternNumbers) to DateTime.parse(commit.t.dropLastPart(), patternNumbersShort)
                    }
                    patternNumbers.canParse(commit.t.dropLastPart()) -> {
                        DateTime.parse(commit.t.dropLastPart(), patternNumbers) to DateTime.parse(commit.t.dropLastPart(2), patternNumbersShort)
                    }
                    patternWords.canParse(commit.t) -> {
                        DateTime.parse(commit.t, patternWords) to DateTime.parse(commit.t.dropLastPart(), patternWordsShort)
                    }
                    else -> {
                        print("ERROR: ${commit.t}")
                        error("Fatal error")
                    }
                }
                rows.add(Row(mapOf(
                        PushEventsTable.author to commit.a,
                        PushEventsTable.language to (repoMap[r.toLowerCase().intern()] ?: "Unknown"),
                        PushEventsTable.repository to r,
                        PushEventsTable.commitMsg to commit.m,
                        PushEventsTable.createdTime to time,
                        PushEventsTable.createdDate to time.toDate(),
                        PushEventsTable.createdLocalTime to localTime,
                        PushEventsTable.createdLocalDate to localTime.toDate())
                        as Map<Column<Any, DbType<Any>>, Any>))
            } catch (e : Exception) {
                print("Error: ${e.message}")
            }
        }
        return rows
    }

}

fun DateTimeFormatter.canParse(str: String): Boolean {
    return try {
        DateTime.parse(str, this)
        true
    } catch (e: Exception) {
        false
    }
}

fun String.dropLastPart(size : Int = 1): String {
    return this.split(" ").dropLast(size).joinToString(separator = " ")
}



data class Commit(val h: String, val a: String, val t: String, val m: String)
