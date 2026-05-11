package com.siridhanya.hub.data.database

import androidx.room.*
import com.siridhanya.hub.data.dao.*
import com.siridhanya.hub.data.entities.*

@Database(
    entities = [MandiPrice::class, Recipe::class, HealthBenefit::class, FpoContact::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mandiPriceDao(): MandiPriceDao
    abstract fun recipeDao(): RecipeDao
    abstract fun healthBenefitDao(): HealthBenefitDao
    abstract fun fpoContactDao(): FpoContactDao
    companion object { const val DB_NAME = "siri_dhanya_db" }
}
