package com.coderbdk.asmaulhusna.di

import android.content.Context
import com.coderbdk.asmaulhusna.playback.AudioPlayerManager
import com.coderbdk.asmaulhusna.playback.MediaAudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMediaAudioPlayer(
        @ApplicationContext context: Context,
    ): AudioPlayerManager {
        return MediaAudioPlayer(context)
    }
}