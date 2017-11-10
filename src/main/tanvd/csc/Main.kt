package tanvd.csc

import tanvd.csc.clickhouse.PushEventsTable
import tanvd.csc.dataset.RepositoryDataset
import java.io.File

fun main(args: Array<String>) {
    println("Loading repository dictionary")
    val repoMap = RepositoryDataset.prepareMap(File("/home/tanvd/CSC/seminar/data/repos/repos_languages_simple.csv"))
    println("Repository dictionary loaded")

    PushEventsTable.syncScheme()
    val directory = File("/home/tanvd/CSC/seminar/data/commits")
    directory.listFiles()
            .filter { it.extension == "json" }
            .forEach { EventsConverter.uploadFile(it, repoMap) }
}