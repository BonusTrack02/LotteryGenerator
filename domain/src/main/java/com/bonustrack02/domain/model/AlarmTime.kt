package com.bonustrack02.domain.model

data class AlarmTime(
    val hour: Int
) {
    init {
        require(hour in 0 .. 23) { "Hour value must be between 0 and 23" }
    }
}
