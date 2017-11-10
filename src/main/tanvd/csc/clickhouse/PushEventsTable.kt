package tanvd.csc.clickhouse

import tanvd.aorm.Database
import tanvd.aorm.Engine
import tanvd.aorm.MergeTree
import tanvd.aorm.Table

object PushEventsTable : Table("PushEventsTableTest") {
    override var db: Database = ExampleDatabase

    val author = string("author")

    val language = string("language")

    val repository = string("repository")

    val commitMsg = string("commit_msg")

    val createdTime = datetime("created_time")

    val createdDate = date("created_date")

    val createdLocalTime = datetime("created_local_time")

    val createdLocalDate = date("created_local_date")

    override val engine: Engine = MergeTree(createdDate, listOf(createdDate, author, commitMsg))
}