package es.salvaaoliiver.secondevproject.main.bottombar.api.room

import androidx.room.Database
import androidx.room.RoomDatabase
import es.salvaaoliiver.secondevproject.main.bottombar.api.`object`.NasaPictureOfDay

@Database(entities = [NasaPictureOfDay::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nasaPictureDao(): NasaPictureDao
}

