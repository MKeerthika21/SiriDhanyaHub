package com.siridhanya.hub.di

import android.content.Context
import androidx.room.Room
import com.siridhanya.hub.data.database.AppDatabase
import dagger.Module; import dagger.Provides; import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, AppDatabase.DB_NAME)
            .fallbackToDestructiveMigration().build()
    @Provides fun mandiDao(db: AppDatabase)  = db.mandiPriceDao()
    @Provides fun recipeDao(db: AppDatabase) = db.recipeDao()
    @Provides fun healthDao(db: AppDatabase) = db.healthBenefitDao()
    @Provides fun fpoDao(db: AppDatabase)    = db.fpoContactDao()
}
