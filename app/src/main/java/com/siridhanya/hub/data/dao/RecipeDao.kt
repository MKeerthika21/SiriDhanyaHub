package com.siridhanya.hub.data.dao

import androidx.room.*
import com.siridhanya.hub.data.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(recipes: List<Recipe>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(recipe: Recipe): Long
    @Update suspend fun update(recipe: Recipe)
    @Query("SELECT * FROM recipes ORDER BY name ASC") fun getAllRecipes(): Flow<List<Recipe>>
    @Query("SELECT * FROM recipes WHERE millet_type = :type ORDER BY name ASC")
    fun getRecipesByMillet(type: String): Flow<List<Recipe>>
    @Query("SELECT * FROM recipes WHERE is_favorite = 1 ORDER BY name ASC")
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :q || '%' OR description LIKE '%' || :q || '%' OR tags LIKE '%' || :q || '%'")
    fun searchRecipes(q: String): Flow<List<Recipe>>
    @Query("UPDATE recipes SET is_favorite = :fav WHERE id = :id")
    suspend fun setFavorite(id: Long, fav: Boolean)
    @Query("SELECT COUNT(*) FROM recipes") suspend fun getCount(): Int
}
