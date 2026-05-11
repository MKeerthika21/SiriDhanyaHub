package com.siridhanya.hub.data.dao

import androidx.room.*
import com.siridhanya.hub.data.entities.HealthBenefit
import kotlinx.coroutines.flow.Flow

@Dao interface HealthBenefitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(items: List<HealthBenefit>)
    @Query("SELECT * FROM health_benefits ORDER BY millet_type ASC") fun getAll(): Flow<List<HealthBenefit>>
    @Query("SELECT * FROM health_benefits WHERE millet_type = :type") fun getForMillet(type: String): Flow<List<HealthBenefit>>
    @Query("SELECT * FROM health_benefits WHERE condition LIKE '%' || :c || '%'") fun getForCondition(c: String): Flow<List<HealthBenefit>>
    @Query("SELECT COUNT(*) FROM health_benefits") suspend fun getCount(): Int
}
