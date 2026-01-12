package com.bonustrack02.lotterygenerator.di

import com.bonustrack02.data.di.AlarmReceiverType
import com.bonustrack02.lotterygenerator.receiver.AlarmReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReceiverModule {
    @Provides
    @AlarmReceiverType
    fun provideAlarmReceiverClass(): Class<*> {
        return AlarmReceiver::class.java
    }
}