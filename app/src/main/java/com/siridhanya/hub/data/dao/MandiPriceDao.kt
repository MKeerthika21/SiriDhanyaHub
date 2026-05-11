package com.siridhanya.hub.data.dao

import androidx.room.*
import com.siridhanya.hub.data.entities.MandiPrice
import kotlinx.coroutines.flow.Flow

@Dao interface MandiPriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(prices: List<MandiPrice>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(price: MandiPrice): Long
    @Query("SELECT * FROM mandi_prices ORDER BY city ASC, millet_type ASC")
    fun getAllPrices(): Flow<List<MandiPrice>>
    @Query("SELECT * FROM mandi_prices WHERE city = :city ORDER BY millet_type ASC")
    fun getPricesForCity(city: String): Flow<List<MandiPrice>>
    @Query("SELECT * FROM mandi_prices WHERE millet_type = :type ORDER BY city ASC")
    fun getPricesForMillet(type: String): Flow<List<MandiPrice>>
    @Query("SELECT DISTINCT city FROM mandi_prices ORDER BY city ASC")
    fun getAllCities(): Flow<List<String>>
    @Query("SELECT COUNT(*) FROM mandi_prices") suspend fun getCount(): Int
    @Query("DELETE FROM mandi_prices") suspend fun deleteAll()
}
