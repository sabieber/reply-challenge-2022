package scheduler

import model.Character
import model.Demon

class Scheduler {
    lateinit var character: Character
    val demons = mutableListOf<Demon>()
    private val turnOrder = mutableListOf<Demon?>()

    var currentTurn = 0
    var currentFragments = 0

    fun challengeDemons(): List<Demon?> {
        for (i in 0..character.maxTurns) {
            checkStaminaRecovery()

            val demon = selectNextDemon()
            if (demon != null) {
                character.attack(demon)
                demon.defeated = true
                currentFragments += demon.calculateRemainingFragmentScore(character.maxTurns - currentTurn)
            }
            turnOrder.add(demon)

            currentTurn++
        }

        println("Score: $currentFragments")

        return turnOrder
    }

    private fun checkStaminaRecovery() {
        turnOrder.asSequence().filter { it != null }.filter { it!!.turnsUntilStaminaRecovery > 0 }
            .forEach {
                it?.let { demon ->
                    demon.turnsUntilStaminaRecovery--
                    if (demon.turnsUntilStaminaRecovery == 0) {
                        character.currentStamina += demon.recoveredStamina
                        if (character.currentStamina > character.maxStamina) {
                            character.currentStamina = character.maxStamina
                        }
                    }
                }
            }
    }

    private fun getAvailableDemons(): List<Demon> {
        return demons.filter { !it.defeated }.filter { it.requiredStamina <= character.currentStamina }
    }

    private fun selectNextDemon(): Demon? {
        val turnsLeft = character.maxTurns - currentTurn

        var maxFragmentScore = 0
        var maxStaminaScore = 0
        getAvailableDemons().forEach { demon ->
            val fragmentScore = demon.calculateRemainingFragmentScore(turnsLeft)
            if (fragmentScore > maxFragmentScore) {
                maxFragmentScore = fragmentScore
            }
            val staminaScore = demon.calculateStaminaScore(turnsLeft)
            if (staminaScore > maxStaminaScore) {
                maxStaminaScore = staminaScore
            }
        }

//        println("Turn $currentTurn: Max Fragment Score: $maxFragmentScore Max Stamina Score: $maxStaminaScore")

        return getAvailableDemons()
            .maxWithOrNull(Comparator.comparing { demon ->
                val score = demon.attackingScore(turnsLeft, maxFragmentScore, maxStaminaScore)
                score / demon.requiredStamina
            })
    }
}