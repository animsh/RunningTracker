package com.animsh.runningtracker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.animsh.runningtracker.db.RunningDatabase
import com.animsh.runningtracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.animsh.runningtracker.other.Constants.KEY_NAME
import com.animsh.runningtracker.other.Constants.KEY_WEIGHT
import com.animsh.runningtracker.other.Constants.RUNNING_DATABASE_NAME
import com.animsh.runningtracker.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideEunDao(db: RunningDatabase) = db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun providesWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)

    @Singleton
    @Provides
    fun providesFirstTimeToggle(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)


}