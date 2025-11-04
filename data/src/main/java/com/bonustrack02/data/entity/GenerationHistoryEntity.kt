package com.bonustrack02.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenerationHistoryEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "number1")
    val number1: Int,

    @ColumnInfo(name = "number2")
    val number2: Int,

    @ColumnInfo(name = "number3")
    val number3: Int,

    @ColumnInfo(name = "number4")
    val number4: Int,

    @ColumnInfo(name = "number5")
    val number5: Int,

    @ColumnInfo(name = "number6")
    val number6: Int,
)