package es.salvaaoliiver.secondevproject.main.bottombar.api.`object`

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "nasa_pictures")
data class NasaPictureOfDay(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerializedName("date") val date: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("hdurl") val hdUrl: String?,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("service_version") val serviceVersion: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)
