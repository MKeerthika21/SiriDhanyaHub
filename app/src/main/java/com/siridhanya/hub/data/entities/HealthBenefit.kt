package com.siridhanya.hub.data.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "health_benefits")
data class HealthBenefit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "millet_type")   val milletType: String,
    @ColumnInfo(name = "title")         val title: String,
    @ColumnInfo(name = "description")   val description: String,
    @ColumnInfo(name = "condition")     val condition: String = "", // Diabetes, Heart, etc.
    @ColumnInfo(name = "icon_emoji")    val iconEmoji: String = "💚",
    @ColumnInfo(name = "source")        val source: String = "ICMR/NIN"
) : Parcelable
