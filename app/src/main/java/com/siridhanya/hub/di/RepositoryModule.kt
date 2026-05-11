package com.siridhanya.hub.di

import com.siridhanya.hub.data.dao.*
import com.siridhanya.hub.data.repository.SiriDhanyaRepository
import dagger.Module; import dagger.Provides
import dagger.hilt.InstallIn; import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides @Singleton
    fun provideRepository(
        mandiDao: MandiPriceDao, recipeDao: RecipeDao,
        healthDao: HealthBenefitDao, fpoDao: FpoContactDao
    ) = SiriDhanyaRepository(mandiDao, recipeDao, healthDao, fpoDao)
}
