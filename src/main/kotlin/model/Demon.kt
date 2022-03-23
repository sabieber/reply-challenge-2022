package model

private const val fragmentsWeight = 1
private const val staminaWeight = 100
private const val turnsToWaitWeight = 200

data class Demon(
    val index: Int,
    val requiredStamina: Int,
    val turnsToRecover: Int,
    val recoveredStamina: Int,
    val turnsForFragments: Int,
    val fragmentsPerTurn: List<Int>,
    var defeated: Boolean = false,
    var turnsUntilStaminaRecovery: Int = turnsToRecover
) {
    fun calculateRemainingFragmentScore(turnsLeft: Int) = fragmentsPerTurn.take(turnsLeft).sum()

    fun calculateStaminaScore(turnsLeft: Int): Int {
        if (turnsLeft < turnsToRecover) {
            return 0
        }
        return (staminaWeight.toFloat() * recoveredStamina.toFloat() / (turnsToWaitWeight.toFloat() * turnsToRecover.toFloat()) * 500).toInt()
    }

    fun attackingScore(turnsLeft: Int, maxFragmentsToEarn: Int, maxStaminaScore: Int): Int {
        // Out of 100
        val weightedFragmentScore = if (maxFragmentsToEarn > 0) {
            calculateRemainingFragmentScore(turnsLeft) / maxFragmentsToEarn * 100
        } else {
            0
        }

        // Out of 100
        val weightedStaminaScore = if (maxStaminaScore > 0) {
            calculateStaminaScore(turnsLeft) / maxStaminaScore * 100
        } else {
            0
        }

        return (weightedFragmentScore + weightedStaminaScore) / 2
    }
}
