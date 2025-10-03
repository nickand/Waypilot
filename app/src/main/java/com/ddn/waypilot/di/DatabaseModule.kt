package com.ddn.waypilot.di

import android.content.Context
import androidx.room.Room
import com.ddn.waypilot.data.TripsRepository
import com.ddn.waypilot.data.TripsRepositoryImpl
import com.ddn.waypilot.data.local.TripsDao
import com.ddn.waypilot.data.local.WaypilotDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): WaypilotDatabase =
        Room.databaseBuilder(ctx, WaypilotDatabase::class.java, "waypilot.db")
            .fallbackToDestructiveMigration() // recrea DB al cambiar version
            .build()

    @Provides
    fun provideTripsDao(db: WaypilotDatabase): TripsDao = db.tripsDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTripsRepository(impl: TripsRepositoryImpl): TripsRepository
}
