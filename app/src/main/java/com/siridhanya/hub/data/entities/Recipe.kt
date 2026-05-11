package com.siridhanya.hub.data.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name")          val name: String,
    @ColumnInfo(name = "kannada_name")  val kannadaName: String = "",
    @ColumnInfo(name = "millet_type")   val milletType: String,
    @ColumnInfo(name = "description")   val description: String,
    @ColumnInfo(name = "ingredients")   val ingredients: String,  // newline-separated
    @ColumnInfo(name = "steps")         val steps: String,        // newline-separated
    @ColumnInfo(name = "prep_time_min") val prepTimeMin: Int = 30,
    @ColumnInfo(name = "cook_time_min") val cookTimeMin: Int = 20,
    @ColumnInfo(name = "servings")      val servings: Int = 4,
    @ColumnInfo(name = "difficulty")    val difficulty: String = "Easy",
    @ColumnInfo(name = "image_url")     val imageUrl: String = "",
    @ColumnInfo(name = "is_favorite")   val isFavorite: Boolean = false,
    @ColumnInfo(name = "calories")      val calories: Int = 0,
    @ColumnInfo(name = "tags")          val tags: String = "" // comma-separated
) : Parcelable
