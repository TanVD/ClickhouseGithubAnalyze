package tanvd.csc.dataset

import tanvd.csc.utils.withProgress
import java.io.BufferedOutputStream
import java.io.File
import java.io.PrintWriter
import java.util.*

object RepositoryDataset {
    fun convertToOneLanguage(fileIn: File, fileOut: File) {
        val output = PrintWriter(BufferedOutputStream(fileOut.outputStream()))
        var first = true
        val languages: ArrayList<String> = ArrayList()
        var numberProcessed = 0
        fileIn.forEachLine { line ->
            if (first) {
                languages.addAll(line.split(",").drop(1))
                first = false
            } else {
                val lineParts = line.split(",")
                output.write(lineParts.first())
                output.write(" ")
                val index = lineParts.drop(1).withIndex().map { it.index to it.value.toDouble() }.maxBy { it.second }?.first ?: -1
                if (index != -1) {
                    output.write(languages[index])
                    output.write("\n")
                } else {
                    output.write("none \n")
                }
            }
            numberProcessed++
            if (numberProcessed % 1000000 == 0) {
                println(".")
            }
        }
        output.close()
    }

    fun prepareMap(fileIn: File) : Map<String, String> {
        val map = IdentityHashMap<String, String>()
        var index  = 0L
        fileIn.forEachLine { line ->
            withProgress(1000000, index) {
                val repository = line.split(" ")[0].toLowerCase().intern()
                val language = line.split(" ")[1].intern()
                map.put(repository, language)
            }
            index++
        }
        return map
    }
}