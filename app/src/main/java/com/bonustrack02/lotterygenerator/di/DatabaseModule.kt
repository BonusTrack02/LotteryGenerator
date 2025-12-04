package com.bonustrack02.lotterygenerator.di

import android.content.Context
import com.bonustrack02.data.dao.GenerationHistoryDao
import com.bonustrack02.data.database.GenerationHistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGenerationHistoryDatabase(@ApplicationContext context: Context): GenerationHistoryDatabase =
        GenerationHistoryDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideGenerationHistoryDao(database: GenerationHistoryDatabase): GenerationHistoryDao =
        database.generationHistoryDao()
}