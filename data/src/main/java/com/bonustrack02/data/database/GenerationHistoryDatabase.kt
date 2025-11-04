package com.bonustrack02.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bonustrack02.data.dao.GenerationHistoryDao
import com.bonustrack02.data.entity.GenerationHistoryEntity

@Database(
    entities = [GenerationHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GenerationHistoryDatabase : RoomDatabase() {
    abstract fun generationHistoryDao(): GenerationHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: GenerationHistoryDatabase? = null

        fun getDatabase(context: Context): GenerationHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GenerationHistoryDatabase::class.java,
                    "generation_history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
