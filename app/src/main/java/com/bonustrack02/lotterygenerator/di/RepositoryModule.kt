package com.bonustrack02.lotterygenerator.di

import com.bonustrack02.data.repository.GenerationHistoryRepositoryImpl
import com.bonustrack02.domain.repository.GenerationHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGenerationHistoryRepository(
        generationHistoryRepositoryImpl: GenerationHistoryRepositoryImpl
    ): GenerationHistoryRepository
}