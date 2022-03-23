package model

data class Character(
    val startStamina: Int,
    val maxStamina: Int,
    val maxTurns: Int,
    val demons: Int,
    var currentStamina: Int = startStamina
) {
    fun attack(demon: Demon?) {
        currentStamina -= demon!!.requiredStamina
    }
}