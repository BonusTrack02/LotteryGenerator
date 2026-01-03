package com.bonustrack02.domain.model

data class AlarmData(
    val hour: Int,
    val isOn: Boolean,
) {
    init {
        require(hour in 0 .. 23) { "Hour value must be between 0 and 23" }
    }
}
