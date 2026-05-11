package com.siridhanya.hub.data.dao

import androidx.room.*
import com.siridhanya.hub.data.entities.FpoContact
import kotlinx.coroutines.flow.Flow

@Dao interface FpoContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<FpoContact>)
    @Query("SELECT * FROM fpo_contacts ORDER BY district ASC") fun getAll(): Flow<List<FpoContact>>
    @Query("SELECT * FROM fpo_contacts WHERE district = :d") fun getByDistrict(d: String): Flow<List<FpoContact>>
    @Query("SELECT COUNT(*) FROM fpo_contacts") suspend fun getCount(): Int
}
