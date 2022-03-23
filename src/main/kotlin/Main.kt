import input.ScenarioParser
import scheduler.Scheduler
import java.io.BufferedReader
import java.io.File

private const val FILE_00 = "00-example.txt"
private const val FILE_01 = "01-the-cloud-abyss.txt"
private const val FILE_02 = "02-iot-island-of-terror.txt"
private const val FILE_03 = "03-etheryum.txt"
private const val FILE_04 = "04-the-desert-of-autonomous-machines.txt"
private const val FILE_05 = "05-androids-armageddon.txt"

private val FILES = listOf<String>(FILE_00, FILE_01, FILE_02, FILE_03, FILE_04, FILE_05)

fun main(args: Array<String>) {
//    processFile(FILE_01)
    FILES.forEach { fileName ->
        processFile(fileName)
    }
}

private fun processFile(fileName: String) {
    println("generating $fileName")
    val bufferedReader: BufferedReader = File("input/$fileName").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    val scheduler = Scheduler()
    ScenarioParser(inputString, scheduler)

    val turnOrder = scheduler.challengeDemons()

    File("output/$fileName").writeText(turnOrder.filter { it != null }.map { it!!.index.toString() }
        .joinToString("\n"))
}