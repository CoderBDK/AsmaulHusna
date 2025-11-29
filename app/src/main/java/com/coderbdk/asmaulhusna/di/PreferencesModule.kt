package com.coderbdk.asmaulhusna.di

import android.content.Context
import com.coderbdk.asmaulhusna.data.local.prefs.SettingsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun provideSettingsPreferences(
        @ApplicationContext context: Context,
    ): SettingsPreferences {
        return SettingsPreferences(context.applicationContext)
    }
}