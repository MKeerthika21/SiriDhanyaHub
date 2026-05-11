package com.siridhanya.hub.data.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "fpo_contacts")
data class FpoContact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name")        val name: String,
    @ColumnInfo(name = "location")    val location: String,
    @ColumnInfo(name = "district")    val district: String,
    @ColumnInfo(name = "phone")       val phone: String,
    @ColumnInfo(name = "millets")     val millets: String, // comma-separated
    @ColumnInfo(name = "price_range") val priceRange: String = "",
    @ColumnInfo(name = "certified")   val certified: Boolean = false,
    @ColumnInfo(name = "description") val description: String = ""
) : Parcelable
