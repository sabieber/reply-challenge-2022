package input

import scheduler.Scheduler

class ScenarioParser(input: String, scheduler: Scheduler) {

    init {
        input.split("\r\n", "\n").filter { it.isNotEmpty() }.forEachIndexed { index, line ->
            if (index == 0) {
                parseHeader(line, scheduler)
            } else {
                parseDemon(index - 1, line, scheduler)
            }
        }
    }

    private fun parseHeader(line: String, scheduler: Scheduler) {
        val cells = line.split(" ")
        scheduler.character = model.Character(cells[0].toInt(), cells[1].toInt(), cells[2].toInt(), cells[3].toInt())
    }

    private fun parseDemon(index: Int, line: String, scheduler: Scheduler) {
        val cells = line.split(" ")
        val fragments = if (cells[3].toInt() > 0) {
            cells.asSequence().drop(4).map { it.toInt() }.toList()
        } else {
            listOf()
        }
        scheduler.demons += model.Demon(
            index,
            cells[0].toInt(),
            cells[1].toInt(),
            cells[2].toInt(),
            cells[3].toInt(),
            fragments
        )
    }

}