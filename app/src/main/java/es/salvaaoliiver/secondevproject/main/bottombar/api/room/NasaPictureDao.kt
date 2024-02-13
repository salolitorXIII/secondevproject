package es.salvaaoliiver.secondevproject.main.bottombar.api.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.salvaaoliiver.secondevproject.main.bottombar.api.`object`.NasaPictureOfDay

@Dao
interface NasaPictureDao {
    @Query("SELECT * FROM nasa_pictures")
    suspend fun getAllImages(): List<NasaPictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<NasaPictureOfDay>)

    @Query("DELETE FROM nasa_pictures")
    suspend fun deleteAll()
}
